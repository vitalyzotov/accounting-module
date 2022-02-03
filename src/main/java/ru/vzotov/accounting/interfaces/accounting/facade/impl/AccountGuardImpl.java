package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.vzotov.banking.domain.model.Account;

@Service
public class AccountGuardImpl implements AccountGuard {
    @Override
    @PreAuthorize("hasAuthority(#entity.owner().authority())")
    public Account accessing(Account entity) {
        return entity;
    }
}
