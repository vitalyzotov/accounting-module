package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.banking.domain.model.Bank;

public class BankDTOAssembler {

    public static AccountingApi.Bank toDTO(Bank bank) {
        return bank == null ? null : new AccountingApi.Bank(
                bank.bankId().value(),
                bank.name(),
                bank.shortName(),
                bank.longName()
        );
    }
}
