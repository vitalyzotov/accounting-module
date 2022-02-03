package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.vzotov.banking.domain.model.BudgetCategory;

@Service
public class BudgetCategoryGuardImpl implements BudgetCategoryGuard {
    @Override
    @PreAuthorize("hasAuthority(#entity.owner().authority())")
    public BudgetCategory accessing(BudgetCategory entity) {
        return entity;
    }
}
