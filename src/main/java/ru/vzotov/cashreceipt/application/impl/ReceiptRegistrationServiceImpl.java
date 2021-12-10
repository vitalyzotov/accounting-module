package ru.vzotov.cashreceipt.application.impl;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.cashreceipt.application.ReceiptNotFoundException;
import ru.vzotov.cashreceipt.application.ReceiptParsingService;
import ru.vzotov.cashreceipt.application.ReceiptRegistrationService;
import ru.vzotov.cashreceipt.application.nalogru2.ReceiptRepositoryNalogru2;
import ru.vzotov.cashreceipt.domain.model.Check;
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.CheckQRCode;
import ru.vzotov.cashreceipt.domain.model.CheckState;
import ru.vzotov.cashreceipt.domain.model.QRCodeCreatedEvent;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.domain.model.QRCodeRepository;
import ru.vzotov.cashreceipt.domain.model.ReceiptRepository;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional("accounting-tx")
public class ReceiptRegistrationServiceImpl implements ReceiptRegistrationService {

    private static final Logger log = LoggerFactory.getLogger(ReceiptRegistrationServiceImpl.class);
    private static final int MAX_LOAD_SIZE = 10;

    /**
     * Максимальная частота попыток загрузки чека в часах.
     * Чек не будет отправляться на загрузку чаще чем указанное значение
     */
    private static final int LOADING_FREQUENCY_MAX = 12;

    /**
     * Репозиторий загруженных чеков.
     * Позволяет работать с чеками в БД.
     */
    private final ReceiptRepository receiptRepository;

    /**
     * Репозиторий зарегистрированных QR кодов.
     * Позволяет работать с QR кодами в БД.
     */
    private final QRCodeRepository qrCodeRepository;

    /**
     * Сервис парсинга данных чека.
     */
    private final ReceiptParsingService receiptParsingService;

    private final ReceiptRepositoryNalogru2 nalogru;

    private final long maxTryCount = 250;

    private final ReceiptSelectionStrategy selectionStrategy = new RandomSelectionStrategy();

    private final ApplicationEventPublisher eventPublisher;

    public ReceiptRegistrationServiceImpl(ReceiptRepository receiptRepository,
                                          QRCodeRepository qrCodeRepository,
                                          @Qualifier("nalogru2Parser") ReceiptParsingService receiptParsingService,
                                          ReceiptRepositoryNalogru2 nalogru,
                                          ApplicationEventPublisher eventPublisher) {
        this.receiptRepository = receiptRepository;
        this.qrCodeRepository = qrCodeRepository;
        this.receiptParsingService = receiptParsingService;
        this.nalogru = nalogru;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public CheckId register(QRCodeData qrCodeData) throws ReceiptNotFoundException, IOException {
        log.debug("Try registering check {}", qrCodeData);

        final CheckQRCode alreadyRegistered = qrCodeRepository.findByQRCodeData(qrCodeData);
        if (alreadyRegistered != null) {
            return alreadyRegistered.checkId();
        }

        final CheckQRCode qrCode = new CheckQRCode(qrCodeData);
        qrCodeRepository.store(qrCode);

        eventPublisher.publishEvent(new QRCodeCreatedEvent(qrCode.checkId()));

        return qrCode.checkId();
    }

    @Override
    public CheckId loadDetails(QRCodeData qrCodeData) throws ReceiptNotFoundException, IOException {
        log.debug("Try loading check details {}", qrCodeData);

        Check check = receiptRepository.findByQRCodeData(qrCodeData);
        if (check != null) {
            return check.checkId();
        }

        CheckQRCode qrCode = qrCodeRepository.findByQRCodeData(qrCodeData);
        if (qrCode == null) {
            throw new ReceiptNotFoundException("QR code is not yet registered");
        }
        Validate.isTrue(CheckState.NEW.equals(qrCode.state()), "Wrong state of check");

        qrCode.tryLoading();
        try {
            try {
                final String checkData = nalogru.findByQRCodeData(qrCodeData);
                if (checkData == null) {
                    throw new ReceiptNotFoundException();
                }

                check = receiptParsingService.parse(checkData);
                if (check == null) {
                    throw new ReceiptNotFoundException();
                }

                //check.associateWithOperations(qrCode.operations());
                receiptRepository.store(check);

                qrCode.markLoaded();
            } catch (IllegalArgumentException ex) {
                log.error("Parse error when parsing check {}", qrCodeData);
                log.error("Parse error", ex);
                throw ex;
            }
        } finally {
            qrCodeRepository.store(qrCode);
        }

        return check.checkId();
    }

    private Set<CheckQRCode> selectCodesForLoading(final Collection<CheckQRCode> codes) {
        return selectionStrategy.select(codes, MAX_LOAD_SIZE);
    }

    @Override
    public void loadNewReceipts() {
        log.info("Start loading new checks");

        final OffsetDateTime threshold = OffsetDateTime.now().minusHours(LOADING_FREQUENCY_MAX);
        log.info("Loading threshold {}", threshold);

        final List<CheckQRCode> allNewCodes = qrCodeRepository.findAllInState(CheckState.NEW).stream()
                // Выбираем только те чеки, которые пытались загрузить раньше порогового интервала,
                // или те которые никогда не пытались загрузить
                .filter((code) -> (code.loadedAt() == null || code.loadedAt().isBefore(threshold)) && (code.loadingTryCount() < maxTryCount))
                .collect(Collectors.toList());
        log.info("Found {} new checks", allNewCodes.size());

        if (allNewCodes.isEmpty()) {
            log.info("There are no suitable new codes. Skip loading.");
        } else {
            final Set<CheckQRCode> codesToLoad = selectCodesForLoading(allNewCodes);

            for (CheckQRCode code : codesToLoad) {
                try {
                    loadDetails(code.code());
                } catch (ReceiptNotFoundException e) {
                    log.error("Unable to load check {} details, check was not found", code);
                } catch (IOException e) {
                    log.error("Unable to load check {} details, I/O error", code);
                }

                waitToPreventFloodLock();
            }
        }
        log.info("Finish loading new checks");
    }

    private void waitToPreventFloodLock() {
        try {
            log.info("Wait 5 seconds between subsequent calls");
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            log.error("Sleep interrupt", e);
        }
    }

}
