package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.AccountDTO;
import ru.vzotov.banking.domain.model.Account;

import java.util.ArrayList;

public class AccountDtoAssembler {
    public static AccountDTO assemble(Account model) {
        return model == null ? null : new AccountDTO(
                model.accountNumber().number(),
                model.name(),
                model.bankId() == null ? null : model.bankId().value(),
                model.currency() == null ? null : model.currency().getCurrencyCode(),
                model.owner() == null? null: model.owner().value(),
                model.aliases() == null ? null : new ArrayList<>(model.aliases().value())
        );
    }
}
