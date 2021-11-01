package ru.vzotov.accounting.domain.model;

import ru.vzotov.accounting.domain.model.BudgetPlan;
import ru.vzotov.accounting.domain.model.BudgetPlanId;
import ru.vzotov.accounting.domain.model.BudgetRuleId;

import java.util.List;

public interface BudgetPlanRepository {
    BudgetPlan find(BudgetPlanId itemId);
    List<BudgetPlan> findAll();
    List<BudgetPlan> findForRule(BudgetRuleId ruleId);
    void store(BudgetPlan item);
    boolean delete(BudgetPlanId itemId);
}
