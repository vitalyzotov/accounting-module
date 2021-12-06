package ru.vzotov.accounting.application.impl;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.application.AccountNotFoundException;
import ru.vzotov.accounting.application.AccountingService;
import ru.vzotov.accounting.domain.model.AccountRepository;
import ru.vzotov.accounting.domain.model.CardOperationRepository;
import ru.vzotov.accounting.domain.model.HoldOperationRepository;
import ru.vzotov.banking.domain.model.OperationCreatedEvent;
import ru.vzotov.accounting.domain.model.OperationRepository;
import ru.vzotov.banking.domain.model.Account;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.CardNumber;
import ru.vzotov.banking.domain.model.CardOperation;
import ru.vzotov.banking.domain.model.HoldId;
import ru.vzotov.banking.domain.model.HoldOperation;
import ru.vzotov.banking.domain.model.HoldOperationRemovedEvent;
import ru.vzotov.banking.domain.model.MccCode;
import ru.vzotov.banking.domain.model.Operation;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.banking.domain.model.PosTerminal;
import ru.vzotov.banking.domain.model.TransactionReference;
import ru.vzotov.domain.model.Money;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AccountingServiceImpl implements AccountingService {

    private static final Logger log = LoggerFactory.getLogger(AccountingServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private CardOperationRepository cardOperationRepository;

    @Autowired
    private HoldOperationRepository holdOperationRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional("accounting-tx")
    public OperationId registerOperation(AccountNumber accountNumber, LocalDate date,
                                         TransactionReference transactionReference, OperationType type, Money amount,
                                         String description) throws AccountNotFoundException {
        Validate.notNull(transactionReference);
        Validate.isTrue(!transactionReference.isHold(), "Временная блокировка средств не является завершенной операцией");

        Validate.notNull(accountNumber);
        Validate.notNull(date);
        Validate.notNull(type);
        Validate.notNull(amount);
        Validate.notNull(description);

        final OperationId opId = new OperationId(
                date,
                type,
                accountNumber,
                amount,
                transactionReference
        );

        Operation operation = operationRepository.find(opId);
        if (operation != null) return operation.operationId();

        Account account = accountRepository.find(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account " + accountNumber.number() + " not found");
        }

        Operation op = new Operation(
                opId,
                date,
                amount,
                type,
                accountNumber,
                description
        );

        op.assignTransaction(transactionReference);

        operationRepository.store(op);
        eventPublisher.publishEvent(new OperationCreatedEvent(op.operationId()));

        return op.operationId();
    }

    @Override
    @Transactional("accounting-tx")
    public OperationId registerCardOperation(OperationId operationId, CardNumber cardNumber, PosTerminal terminal,
                                             LocalDate authDate, LocalDate purchaseDate, Money amount, String extraInfo,
                                             MccCode mcc) {
        Validate.notNull(operationId);
        Validate.notNull(cardNumber);
        // Validate.notNull(terminal); // may be null
        Validate.notNull(authDate);
        Validate.notNull(purchaseDate);
        Validate.notNull(amount);
        Validate.notNull(mcc);

        final CardOperation operation = cardOperationRepository.find(operationId);
        if (operation != null) return operation.operationId();

        final CardOperation op = new CardOperation(
                operationId,
                cardNumber,
                terminal,
                authDate,
                purchaseDate,
                amount,
                extraInfo,
                mcc
        );

        cardOperationRepository.store(op);

        return op.operationId();
    }

    @Override
    @Transactional("accounting-tx")
    public void deleteHoldOperation(HoldOperation hold) {
        log.debug("Notify subscribers that the hold {} is about to be removed", hold);
        eventPublisher.publishEvent(new HoldOperationRemovedEvent(hold));

        log.debug("Remove the hold {}", hold);
        holdOperationRepository.delete(hold);
    }

    @Override
    @Transactional("accounting-tx")
    public void removeMatchingHoldOperations(OperationId operationId) {
        log.debug("Remove hold operations for id {}", operationId);
        Validate.notNull(operationId);

        Operation operation = operationRepository.find(operationId);
        Validate.notNull(operation);

        CardOperation cardOperation = cardOperationRepository.find(operationId);
        log.debug("Card operation is {}", cardOperation);

        List<LocalDate> dates = Stream.of(
                        operation.date(),
                        operation.authorizationDate(),
                        cardOperation == null ? null : cardOperation.authDate(),
                        cardOperation == null ? null : cardOperation.purchaseDate()
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        LocalDate from = dates.stream().min(LocalDate::compareTo).orElse(operation.date());
        LocalDate to = dates.stream().max(LocalDate::compareTo).orElse(operation.date());
        log.debug("Search holds from {} to {}", from, to);

        List<HoldOperation> holds = holdOperationRepository.findByAccountAndDate(operation.account(), from, to);
        log.debug("There are {} holds found", holds.size());

        for (HoldOperation hold : holds) {
            if ((cardOperation == null && hold.matches(operation)) ||
                    (cardOperation != null && hold.matches(operation, cardOperation))) {

                deleteHoldOperation(hold);

                break;
            }
        }
    }

    @Override
    @Transactional("accounting-tx")
    public HoldOperation registerHoldOperation(AccountNumber accountNumber, LocalDate date,
                                               OperationType type, Money amount, String description) throws AccountNotFoundException {
        Validate.notNull(accountNumber);
        Validate.notNull(date);
        Validate.notNull(type);
        Validate.notNull(amount);
        Validate.notNull(description);

        Account account = accountRepository.find(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException("Account " + accountNumber.number() + " not found");
        }

        HoldOperation op;

        op = holdOperationRepository.find(accountNumber, date, amount, type, description);
        if (op == null) {
            op = new HoldOperation(HoldId.nextId(), accountNumber, date, amount, type, description, null, null);
            holdOperationRepository.store(op);
        }

        return op;
    }
}
