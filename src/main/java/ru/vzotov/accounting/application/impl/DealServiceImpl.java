package ru.vzotov.accounting.application.impl;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.application.DealService;
import ru.vzotov.accounting.domain.model.AccountRepository;
import ru.vzotov.accounting.domain.model.CardOperationRepository;
import ru.vzotov.accounting.domain.model.Deal;
import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.domain.model.DealRepository;
import ru.vzotov.accounting.domain.model.OperationRepository;
import ru.vzotov.banking.domain.model.Account;
import ru.vzotov.banking.domain.model.BankingEvents;
import ru.vzotov.banking.domain.model.CardOperation;
import ru.vzotov.banking.domain.model.Operation;
import ru.vzotov.banking.domain.model.TransactionCreatedEvent;
import ru.vzotov.cashreceipt.domain.model.QRCode;
import ru.vzotov.cashreceipt.domain.model.QRCodeCreatedEvent;
import ru.vzotov.cashreceipt.domain.model.QRCodeRepository;
import ru.vzotov.domain.model.Money;

import java.util.Collections;
import java.util.Optional;

@Service
public class DealServiceImpl implements DealService {

    private static final Logger log = LoggerFactory.getLogger(DealService.class);

    private final AccountRepository accountRepository;

    private final OperationRepository operationRepository;

    private final CardOperationRepository cardOperationRepository;

    private final QRCodeRepository qrCodeRepository;

    private final DealRepository dealRepository;

    public DealServiceImpl(AccountRepository accountRepository,
                           OperationRepository operationRepository,
                           CardOperationRepository cardOperationRepository,
                           QRCodeRepository qrCodeRepository,
                           DealRepository dealRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
        this.cardOperationRepository = cardOperationRepository;
        this.qrCodeRepository = qrCodeRepository;
        this.dealRepository = dealRepository;
    }

    @EventListener
    @Transactional("accounting-tx")
    public void onOperationCreated(BankingEvents.OperationCreatedEvent event) {
        Validate.notNull(event);
        Operation operation = operationRepository.find(event.operationId());
        if (operation == null) {
            log.error("Unable to create deal for non-existing operation {}", event.operationId().idString());
            return;
        }

        createDealForOperation(operation);
    }

    @EventListener
    @Transactional("accounting-tx")
    public void onCardOperationCreated(BankingEvents.CardOperationCreatedEvent event) {
        Validate.notNull(event);
        CardOperation operation = cardOperationRepository.find(event.operationId());
        if(operation == null) {
            log.error("Unable to modify deal for non-existing card operation {}", event.operationId().idString());
        }

        modifyDealForCardOperation(operation);
    }

    @EventListener
    @Transactional(value = "accounting-tx")
    public void onQRCodeCreated(QRCodeCreatedEvent event) {
        Validate.notNull(event);
        QRCode qrCode = qrCodeRepository.find(event.receiptId());
        if (qrCode == null) {
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

        if (secondary.date().isBefore(primary.date())) {
            Deal d = primary;
            primary = secondary;
            secondary = d;
        }

        primary.join(secondary);
        primary.setAmount(primary.amount().subtract(primary.amount()));

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

        final Money amount = switch (operation.type()) {
            case WITHDRAW -> operation.amount().negate();
            case DEPOSIT -> operation.amount();
            default -> throw new IllegalArgumentException();
        };
        final Account account = accountRepository.find(operation.account());
        final CardOperation cardOperation = cardOperationRepository.find(operation.operationId());

        final Deal deal = new Deal(
                DealId.nextId(),
                account.owner(),
                Optional.ofNullable(cardOperation).map(CardOperation::purchaseDate).orElse(operation.date()),
                amount,
                operation.description(),
                operation.comment(),
                operation.category(),
                Collections.emptySet(),
                Collections.singleton(operation.operationId()),
                Collections.emptySet(),
                Collections.emptyList()
        );
        dealRepository.store(deal);
    }

    private void modifyDealForCardOperation(CardOperation cardOperation) {
        Validate.notNull(cardOperation, "Null card operation not allowed");

        final Deal deal = dealRepository.findByOperation(cardOperation.operationId());
        final Operation operation = operationRepository.find(cardOperation.operationId());
        if(operation == null) {
            log.error("Unable to process event for card operation {}", cardOperation.operationId());
        } else {
            if (deal == null) {
                createDealForOperation(operation);
            } else {
                deal.setDate(Optional.of(cardOperation).map(CardOperation::purchaseDate).orElse(operation.date()));
            }
        }
    }

    private void createDealForQrCode(QRCode qrCode) {
        log.info("Automatically create deal for QR code {}", qrCode.receiptId());
        final Money amount = switch (qrCode.code().operationType()) {
            case INCOME, EXPENSE_RETURN -> qrCode.code().totalSum().negate();
            case EXPENSE, INCOME_RETURN -> qrCode.code().totalSum();
            default -> throw new IllegalArgumentException();
        };
        final Deal deal = new Deal(
                DealId.nextId(),
                qrCode.owner(),
                qrCode.code().dateTime().value().toLocalDate(),
                amount,
                qrCode.code().fiscalSign().toString(),
                null,
                null,
                Collections.singleton(qrCode.receiptId()),
                Collections.emptySet(),
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
