package ru.vzotov.accounting.domain.model;

import ru.vzotov.person.domain.model.PersonId;

import java.util.List;

public interface BudgetRepository {
    BudgetRule findRule(BudgetRuleId ruleId);

    Budget findForRule(BudgetRuleId ruleId);

    Budget find(BudgetId budgetId);

    List<Budget> findAll();

    List<Budget> find(PersonId owner);

    void store(Budget budget);

    boolean delete(BudgetId budgetId);
}
