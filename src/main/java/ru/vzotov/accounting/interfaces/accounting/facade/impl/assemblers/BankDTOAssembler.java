package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.BankDTO;
import ru.vzotov.banking.domain.model.Bank;

public class BankDTOAssembler {

    public static BankDTO toDTO(Bank bank) {
        return bank == null ? null : new BankDTO(
                bank.bankId().value(),
                bank.name(),
                bank.shortName(),
                bank.longName()
        );
    }
}
