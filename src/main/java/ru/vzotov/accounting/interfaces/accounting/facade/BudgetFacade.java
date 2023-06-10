package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.domain.model.BudgetId;
import ru.vzotov.accounting.domain.model.BudgetPlanId;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.Budget;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.BudgetPlan;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.BudgetRule;
import ru.vzotov.accounting.interfaces.common.CommonApi.Money;

import java.time.LocalDate;
import java.util.List;

public interface BudgetFacade {
    List<Budget> listBudgets();

    Budget getBudget(BudgetId budgetId);

    Budget createBudget(BudgetId budgetId, String name, String currency, String locale);

    Budget modifyBudget(BudgetId budgetId, String name, String currency, String locale) throws BudgetNotFoundException;

    Budget deleteBudget(BudgetId budgetId) throws BudgetNotFoundException;

    BudgetRule getBudgetRule(BudgetId budgetId, String ruleId) throws BudgetNotFoundException;

    Budget addRuleToBudget(BudgetId budgetId, BudgetRule rule) throws BudgetNotFoundException;

    Budget deleteRuleFromBudget(BudgetId budgetId, String ruleId) throws BudgetNotFoundException;

    Budget replaceBudgetRule(BudgetId budgetId, String ruleId, BudgetRule rule) throws BudgetNotFoundException;

    List<BudgetPlan> listPlans(BudgetId budgetId);

    BudgetPlan getPlan(BudgetPlanId itemId);

    BudgetPlan createPlan(
            String direction, String sourceAccount, String targetAccount,
            Long categoryId, String purchaseCategoryId, Money value, String ruleId, LocalDate date
    );

    BudgetPlan modifyPlan(
            String itemId, String direction, String sourceAccount, String targetAccount,
            Long categoryId, String purchaseCategoryId, Money value, String ruleId, LocalDate date
    ) throws BudgetPlanNotFoundException;

    BudgetPlan deletePlan(BudgetPlanId itemId);
}
