package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetPlanNotFoundException;
import ru.vzotov.accounting.domain.model.BudgetId;
import ru.vzotov.accounting.domain.model.BudgetPlanId;

import java.time.LocalDate;
import java.util.List;

public interface BudgetFacade {
    List<AccountingApi.Budget> listBudgets();

    AccountingApi.Budget getBudget(BudgetId budgetId);

    AccountingApi.Budget createBudget(BudgetId budgetId, String name, String currency, String locale);

    AccountingApi.Budget modifyBudget(BudgetId budgetId, String name, String currency, String locale) throws BudgetNotFoundException;

    AccountingApi.Budget deleteBudget(BudgetId budgetId) throws BudgetNotFoundException;

    AccountingApi.BudgetRule getBudgetRule(BudgetId budgetId, String ruleId) throws BudgetNotFoundException;

    AccountingApi.Budget addRuleToBudget(BudgetId budgetId, AccountingApi.BudgetRule rule) throws BudgetNotFoundException;

    AccountingApi.Budget deleteRuleFromBudget(BudgetId budgetId, String ruleId) throws BudgetNotFoundException;

    AccountingApi.Budget replaceBudgetRule(BudgetId budgetId, String ruleId, AccountingApi.BudgetRule rule) throws BudgetNotFoundException;

    List<AccountingApi.BudgetPlan> listPlans(BudgetId budgetId);

    AccountingApi.BudgetPlan getPlan(BudgetPlanId itemId);

    AccountingApi.BudgetPlan createPlan(
            String direction, String sourceAccount, String targetAccount,
            Long categoryId, String purchaseCategoryId, AccountingApi.Money value, String ruleId, LocalDate date
    );

    AccountingApi.BudgetPlan modifyPlan(
            String itemId, String direction, String sourceAccount, String targetAccount,
            Long categoryId, String purchaseCategoryId, AccountingApi.Money value, String ruleId, LocalDate date
    ) throws BudgetPlanNotFoundException;

    AccountingApi.BudgetPlan deletePlan(BudgetPlanId itemId);
}
