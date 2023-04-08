package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetPlanDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetPlanNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetRuleDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.MoneyDTO;
import ru.vzotov.accounting.domain.model.BudgetId;
import ru.vzotov.accounting.domain.model.BudgetPlanId;

import java.time.LocalDate;
import java.util.List;

public interface BudgetFacade {
    List<BudgetDTO> listBudgets();

    BudgetDTO getBudget(BudgetId budgetId);

    BudgetDTO createBudget(BudgetId budgetId, String name, String currency, String locale);

    BudgetDTO modifyBudget(BudgetId budgetId, String name, String currency, String locale) throws BudgetNotFoundException;

    BudgetDTO deleteBudget(BudgetId budgetId) throws BudgetNotFoundException;

    BudgetRuleDTO getBudgetRule(BudgetId budgetId, String ruleId) throws BudgetNotFoundException;

    BudgetDTO addRuleToBudget(BudgetId budgetId, BudgetRuleDTO rule) throws BudgetNotFoundException;

    BudgetDTO deleteRuleFromBudget(BudgetId budgetId, String ruleId) throws BudgetNotFoundException;

    BudgetDTO replaceBudgetRule(BudgetId budgetId, String ruleId, BudgetRuleDTO rule) throws BudgetNotFoundException;

    List<BudgetPlanDTO> listPlans(BudgetId budgetId);

    BudgetPlanDTO getPlan(BudgetPlanId itemId);

    BudgetPlanDTO createPlan(
            String direction, String sourceAccount, String targetAccount,
            Long categoryId, String purchaseCategoryId, MoneyDTO value, String ruleId, LocalDate date
    );

    BudgetPlanDTO modifyPlan(
            String itemId, String direction, String sourceAccount, String targetAccount,
            Long categoryId, String purchaseCategoryId, MoneyDTO value, String ruleId, LocalDate date
    ) throws BudgetPlanNotFoundException;

    BudgetPlanDTO deletePlan(BudgetPlanId itemId);
}
