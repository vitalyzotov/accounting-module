package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.banking.domain.model.Account;

import java.util.ArrayList;

public class AccountDtoAssembler {
    public static AccountingApi.Account assemble(Account model) {
        return model == null ? null : new AccountingApi.Account(
                model.accountNumber().number(),
                model.name(),
                model.bankId() == null ? null : model.bankId().value(),
                model.currency() == null ? null : model.currency().getCurrencyCode(),
                model.owner() == null? null: model.owner().value(),
                model.aliases() == null ? null : new ArrayList<>(model.aliases().value())
        );
    }
}
