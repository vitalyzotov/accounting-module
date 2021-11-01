package ru.vzotov.accounting.application;

import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.CardNumber;
import ru.vzotov.banking.domain.model.HoldOperation;
import ru.vzotov.banking.domain.model.MccCode;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.banking.domain.model.PosTerminal;
import ru.vzotov.banking.domain.model.TransactionReference;
import ru.vzotov.domain.model.Money;

import java.time.LocalDate;

public interface AccountingService {

    OperationId registerOperation(
            AccountNumber accountNumber,
            LocalDate date,
            TransactionReference transactionReference,
            OperationType type,
            Money amount,
            String description
    ) throws AccountNotFoundException;

    OperationId registerCardOperation(
            OperationId operationId,
            CardNumber cardNumber,
            PosTerminal terminal,
            LocalDate authDate,
            LocalDate purchaseDate,
            Money amount,
            String extraInfo,
            MccCode mcc
    );

    void removeMatchingHoldOperations(OperationId operationId);

    void deleteHoldOperation(HoldOperation hold);

    HoldOperation registerHoldOperation(
            AccountNumber accountNumber,
            LocalDate date,
            OperationType type,
            Money amount,
            String description
    ) throws AccountNotFoundException;
}
