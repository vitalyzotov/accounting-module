package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import ru.vzotov.banking.domain.model.BudgetCategory;

public interface BudgetCategoryGuard {
    BudgetCategory accessing(BudgetCategory entity);
}
