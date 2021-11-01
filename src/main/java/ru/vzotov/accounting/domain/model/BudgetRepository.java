package ru.vzotov.accounting.domain.model;

import ru.vzotov.accounting.domain.model.Budget;
import ru.vzotov.accounting.domain.model.BudgetId;
import ru.vzotov.accounting.domain.model.BudgetRule;
import ru.vzotov.accounting.domain.model.BudgetRuleId;

import java.util.List;

public interface BudgetRepository {
    BudgetRule findRule(BudgetRuleId ruleId);
    Budget find(BudgetId budgetId);
    List<Budget> findAll();
    void store(Budget budget);
    boolean delete(BudgetId budgetId);
}
