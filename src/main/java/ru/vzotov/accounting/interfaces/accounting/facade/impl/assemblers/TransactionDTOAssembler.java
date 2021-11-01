package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.TransactionDTO;
import ru.vzotov.banking.domain.model.Transaction;

public class TransactionDTOAssembler {
    public static TransactionDTO toDTO(Transaction tx) {
        return tx == null ? null : new TransactionDTO(
                tx.primaryOperation().idString(),
                tx.secondaryOperation().idString()
        );
    }

}
