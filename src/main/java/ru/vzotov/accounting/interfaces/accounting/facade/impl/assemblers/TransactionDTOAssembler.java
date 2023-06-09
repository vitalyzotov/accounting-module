package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.banking.domain.model.Transaction;

public class TransactionDTOAssembler {
    public static AccountingApi.Transaction toDTO(Transaction tx) {
        return tx == null ? null : new AccountingApi.Transaction(
                tx.primaryOperation().idString(),
                tx.secondaryOperation().idString()
        );
    }

}
