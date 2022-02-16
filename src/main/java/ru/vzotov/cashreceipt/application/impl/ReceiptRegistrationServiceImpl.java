package ru.vzotov.cashreceipt.application.impl;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.infrastructure.security.SecurityUtils;
import ru.vzotov.cashreceipt.application.ReceiptNotFoundException;
import ru.vzotov.cashreceipt.application.ReceiptParsingService;
import ru.vzotov.cashreceipt.application.ReceiptRegistrationService;
import ru.vzotov.cashreceipt.application.nalogru2.ReceiptRepositoryNalogru2;
import ru.vzotov.cashreceipt.domain.model.Receipt;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.cashreceipt.domain.model.QRCode;
import ru.vzotov.cashreceipt.domain.model.ReceiptState;
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
    @Transactional("accounting-tx")
    @Secured({"ROLE_USER"})
    public ReceiptId register(QRCodeData qrCodeData) throws ReceiptNotFoundException, IOException {
        log.debug("Try registering receipt {}", qrCodeData);

        final QRCode alreadyRegistered = qrCodeRepository.findByQRCodeData(qrCodeData);
        if (alreadyRegistered != null) {
            return alreadyRegistered.receiptId();
        }

        final QRCode qrCode = new QRCode(qrCodeData, SecurityUtils.getCurrentPerson());
        qrCodeRepository.store(qrCode);

        eventPublisher.publishEvent(new QRCodeCreatedEvent(qrCode.receiptId()));

        return qrCode.receiptId();
    }

    @Override
    @Transactional("accounting-tx")
    public ReceiptId loadDetails(QRCodeData qrCodeData) throws ReceiptNotFoundException, IOException {
        log.debug("Try loading receipt details {}", qrCodeData);

        Receipt receipt = receiptRepository.findByQRCodeData(qrCodeData);
        if (receipt != null) {
            return receipt.receiptId();
        }

        QRCode qrCode = qrCodeRepository.findByQRCodeData(qrCodeData);
        if (qrCode == null) {
            throw new ReceiptNotFoundException("QR code is not yet registered");
        }
        Validate.isTrue(ReceiptState.NEW.equals(qrCode.state()), "Wrong state of receipt");

        qrCode.tryLoading();
        try {
            try {
                final String receiptData = nalogru.findByQRCodeData(qrCodeData);
                if (receiptData == null) {
                    throw new ReceiptNotFoundException();
                }

                receipt = receiptParsingService.parse(qrCode.owner(), receiptData);
                if (receipt == null) {
                    throw new ReceiptNotFoundException();
                }

                receiptRepository.store(receipt);

                qrCode.markLoaded();
            } catch (IllegalArgumentException ex) {
                log.error("Parse error when parsing receipt {}", qrCodeData);
                log.error("Parse error", ex);
                throw ex;
            }
        } finally {
            qrCodeRepository.store(qrCode);
        }

        return receipt.receiptId();
    }

    private Set<QRCode> selectCodesForLoading(final Collection<QRCode> codes) {
        return selectionStrategy.select(codes, MAX_LOAD_SIZE);
    }

    @Override
    @Transactional("accounting-tx")
    public void loadNewReceipts() {
        log.info("Start loading new receipts");

        final OffsetDateTime threshold = OffsetDateTime.now().minusHours(LOADING_FREQUENCY_MAX);
        log.info("Loading threshold {}", threshold);

        final List<QRCode> allNewCodes = qrCodeRepository.findAllInState(ReceiptState.NEW).stream()
                // Select receipts, that we never tried to load, or that we tried to load before threshold
                .filter((code) -> (code.loadedAt() == null || code.loadedAt().isBefore(threshold)) && (code.loadingTryCount() < maxTryCount))
                .collect(Collectors.toList());
        log.info("Found {} new receipts", allNewCodes.size());

        if (allNewCodes.isEmpty()) {
            log.info("There are no suitable new codes. Skip loading.");
        } else {
            final Set<QRCode> codesToLoad = selectCodesForLoading(allNewCodes);

            for (QRCode code : codesToLoad) {
                try {
                    loadDetails(code.code());
                } catch (ReceiptNotFoundException e) {
                    log.error("Unable to load receipt {} details, receipt was not found", code);
                } catch (IOException e) {
                    log.error("Unable to load receipt {} details, I/O error", code);
                }

                waitToPreventFloodLock();
            }
        }
        log.info("Finish loading new receipts");
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
