package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import ru.vzotov.banking.domain.model.Account;

public interface AccountGuard {
    Account accessing(Account entity);
}
