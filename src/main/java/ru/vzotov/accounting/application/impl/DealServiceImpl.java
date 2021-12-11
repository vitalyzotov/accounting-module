package ru.vzotov.accounting.application.impl;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.application.DealService;
import ru.vzotov.accounting.domain.model.Deal;
import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.domain.model.DealRepository;
import ru.vzotov.accounting.domain.model.OperationRepository;
import ru.vzotov.banking.domain.model.Operation;
import ru.vzotov.banking.domain.model.OperationCreatedEvent;
import ru.vzotov.banking.domain.model.TransactionCreatedEvent;
import ru.vzotov.cashreceipt.domain.model.CheckQRCode;
import ru.vzotov.cashreceipt.domain.model.QRCodeCreatedEvent;
import ru.vzotov.cashreceipt.domain.model.QRCodeRepository;
import ru.vzotov.domain.model.Money;

import java.util.Collections;
import java.util.Optional;

@Service
public class DealServiceImpl implements DealService {

    private static final Logger log = LoggerFactory.getLogger(DealService.class);

    private final OperationRepository operationRepository;

    private final QRCodeRepository qrCodeRepository;

    private final DealRepository dealRepository;

    public DealServiceImpl(OperationRepository operationRepository,
                           QRCodeRepository qrCodeRepository,
                           DealRepository dealRepository) {
        this.operationRepository = operationRepository;
        this.qrCodeRepository = qrCodeRepository;
        this.dealRepository = dealRepository;
    }

    @EventListener
    @Transactional("accounting-tx")
    public void onOperationCreated(OperationCreatedEvent event) {
        Validate.notNull(event);
        Operation operation = operationRepository.find(event.operationId());
        if (operation == null) {
            log.error("Unable to create deal for non-existing operation {}", event.operationId().idString());
            return;
        }

        createDealForOperation(operation);
    }

    @EventListener
    @Transactional(value = "accounting-tx")
    public void onQRCodeCreated(QRCodeCreatedEvent event) {
        Validate.notNull(event);
        CheckQRCode qrCode = qrCodeRepository.find(event.receiptId());
        if(qrCode == null) {
            log.error("Unable to create deal. QR code not found: {}", event.receiptId().value());
            return;
        }

        createDealForQrCode(qrCode);
    }

    @EventListener
    @Transactional("accounting-tx")
    public void onTransactionCreated(TransactionCreatedEvent event) {
        Validate.notNull(event);

        Deal primary = dealRepository.findByOperation(event.primary());
        Validate.notNull(primary);

        Deal secondary = dealRepository.findByOperation(event.secondary());
        Validate.notNull(secondary);

        primary.join(secondary);
        dealRepository.delete(secondary);
        dealRepository.store(primary);
    }

    @Scheduled(initialDelay = 40 * 1000, fixedDelay = 30 * 60 * 1000)
    @Transactional("accounting-tx")
    public void createDealsForOperations() {
        log.info("Start: Automatically create deals for operations and receipts");
        try {
            processOperationsWithoutDeals();
            processReceiptsWithoutDeals();
        } finally {
            log.info("Finish: Automatically create deals for operations and receipts");
        }
    }

    private void createDealForOperation(Operation operation) {
        log.info("Automatically create deal for operation {}", operation.operationId());

        Money amount;
        switch (operation.type()) {
            case WITHDRAW:
                amount = operation.amount().negate();
                break;
            case DEPOSIT:
                amount = operation.amount();
                break;
            default:
                throw new IllegalArgumentException();
        }

        final Deal deal = new Deal(
                DealId.nextId(),
                Optional.ofNullable(operation.authorizationDate()).orElse(operation.date()),
                amount,
                operation.description(),
                operation.comment(),
                operation.category(),
                Collections.emptySet(),
                Collections.singleton(operation.operationId()),
                Collections.emptyList()
        );
        dealRepository.store(deal);
    }

    private void createDealForQrCode(CheckQRCode qrCode) {
        log.info("Automatically create deal for QR code {}", qrCode.checkId());
        Money amount;
        switch (qrCode.code().operationType()) {
            case INCOME:
            case EXPENSE_RETURN:
                amount = qrCode.code().totalSum().negate();
                break;
            case EXPENSE:
            case INCOME_RETURN:
                amount = qrCode.code().totalSum();
                break;
            default:
                throw new IllegalArgumentException();
        }
        final Deal deal = new Deal(
                DealId.nextId(),
                qrCode.code().dateTime().value().toLocalDate(),
                amount,
                qrCode.code().fiscalSign().toString(),
                null,
                null,
                Collections.singleton(qrCode.checkId()),
                Collections.emptySet(),
                Collections.emptyList()
        );
        dealRepository.store(deal);
    }

    private void processOperationsWithoutDeals() {
        operationRepository.findWithoutDeals().forEach(this::createDealForOperation);
    }

    private void processReceiptsWithoutDeals() {
        qrCodeRepository.findWithoutDeals().forEach(this::createDealForQrCode);
    }
}