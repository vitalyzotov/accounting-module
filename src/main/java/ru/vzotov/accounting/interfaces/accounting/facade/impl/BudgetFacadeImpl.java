package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.domain.model.Budget;
import ru.vzotov.accounting.domain.model.BudgetDirection;
import ru.vzotov.accounting.domain.model.BudgetId;
import ru.vzotov.accounting.domain.model.BudgetPlan;
import ru.vzotov.accounting.domain.model.BudgetPlanId;
import ru.vzotov.accounting.domain.model.BudgetPlanRepository;
import ru.vzotov.accounting.domain.model.BudgetRepository;
import ru.vzotov.accounting.domain.model.BudgetRule;
import ru.vzotov.accounting.domain.model.BudgetRuleId;
import ru.vzotov.accounting.domain.model.BudgetRuleType;
import ru.vzotov.accounting.domain.model.Calculation;
import ru.vzotov.accounting.infrastructure.security.SecurityUtils;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.accounting.facade.BudgetFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetPlanNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.BudgetDTOAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.BudgetPlanDTOAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.BudgetRuleDTOAssembler;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.BudgetCategoryId;
import ru.vzotov.calendar.domain.model.Recurrence;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;

import java.time.LocalDate;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetFacadeImpl implements BudgetFacade {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetPlanRepository budgetPlanRepository;

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<AccountingApi.Budget> listBudgets() {
        return budgetRepository.find(SecurityUtils.getCurrentPerson()).stream().map(BudgetDTOAssembler::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountingApi.Budget getBudget(BudgetId budgetId) {
        return BudgetDTOAssembler.toDTO(findSecurely(budgetId));
    }

    @PostAuthorize("hasAuthority(returnObject.owner)")
    private Budget findSecurely(BudgetId budgetId) {
        return budgetRepository.find(budgetId);
    }

    @PostAuthorize("hasAuthority(returnObject.owner)")
    private Budget findSecurely(BudgetRuleId id) {
        return budgetRepository.findForRule(id);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountingApi.Budget createBudget(BudgetId budgetId, String name, String currency, String locale) {
        final Budget budget = new Budget(budgetId, SecurityUtils.getCurrentPerson(),
                name, new HashSet<>(), Currency.getInstance(currency), locale);
        budgetRepository.store(budget);
        return BudgetDTOAssembler.toDTO(budget);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountingApi.Budget modifyBudget(BudgetId budgetId, String name, String currency, String locale) throws BudgetNotFoundException {
        final Budget budget = findSecurely(budgetId);
        if (budget == null) throw new BudgetNotFoundException();
        budget.setName(name);
        budget.setCurrency(Currency.getInstance(currency));
        budget.setLocale(locale);
        budgetRepository.store(budget);
        return BudgetDTOAssembler.toDTO(budget);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountingApi.Budget deleteBudget(BudgetId budgetId) throws BudgetNotFoundException {
        final Budget budget = findSecurely(budgetId);
        if (budget == null) throw new BudgetNotFoundException();
        return budgetRepository.delete(budgetId) ? BudgetDTOAssembler.toDTO(budget) : null;
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountingApi.BudgetRule getBudgetRule(BudgetId budgetId, String ruleId) throws BudgetNotFoundException {
        Validate.notNull(budgetId);
        Validate.notNull(ruleId);

        final Budget budget = findSecurely(budgetId);
        if (budget == null) throw new BudgetNotFoundException();

        return budget.rules().stream()
                .filter(r -> ruleId.equals(r.ruleId().value()))
                .map(BudgetRuleDTOAssembler::toDTO)
                .findFirst()
                .orElse(null);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountingApi.Budget addRuleToBudget(BudgetId budgetId, AccountingApi.BudgetRule rule) throws BudgetNotFoundException {
        final Budget budget = findSecurely(budgetId);
        if (budget == null) throw new BudgetNotFoundException();
        BudgetRule r = createRuleFromDTO(rule);
        budget.addRule(r);
        budgetRepository.store(budget);
        return BudgetDTOAssembler.toDTO(findSecurely(budgetId));
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountingApi.Budget deleteRuleFromBudget(BudgetId budgetId, String ruleId) throws BudgetNotFoundException {
        Validate.notNull(budgetId);
        Validate.notNull(ruleId);

        final Budget budget = findSecurely(budgetId);
        if (budget == null) throw new BudgetNotFoundException();

        budget.rules().stream()
                .filter(r -> ruleId.equals(r.ruleId().value()))
                .collect(Collectors.toList())
                .forEach(budget::deleteRule);

        budgetRepository.store(budget);
        return BudgetDTOAssembler.toDTO(findSecurely(budgetId));
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountingApi.Budget replaceBudgetRule(BudgetId budgetId, String ruleId, AccountingApi.BudgetRule rule) throws BudgetNotFoundException {
        final Budget budget = findSecurely(budgetId);
        if (budget == null) throw new BudgetNotFoundException();

        budget.rules().stream()
                .filter(r -> ruleId.equals(r.ruleId().value()))
                .collect(Collectors.toSet())
                .forEach(budget::deleteRule);
        budgetRepository.store(budget);

        final BudgetRule r = createRuleFromDTO(rule);
        budget.addRule(r);

        budgetRepository.store(budget);
        return BudgetDTOAssembler.toDTO(findSecurely(budgetId));
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<AccountingApi.BudgetPlan> listPlans(BudgetId budgetId) {
        final Budget budget = findSecurely(budgetId);
        return budget.rules().stream()
                .map(ru.vzotov.accounting.domain.model.BudgetRule::ruleId)
                .flatMap(ruleId -> budgetPlanRepository.findForRule(ruleId).stream())
                .map(BudgetPlanDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountingApi.BudgetPlan getPlan(BudgetPlanId itemId) {
        Validate.notNull(itemId);
        final BudgetPlan plan = budgetPlanRepository.find(itemId);
        final Budget budget = findSecurely(plan.rule().ruleId());
        Validate.notNull(budget, "Budget not found");
        return BudgetPlanDTOAssembler.toDTO(plan);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountingApi.BudgetPlan createPlan(String direction, String sourceAccount, String targetAccount,
                                               Long categoryId, String purchaseCategoryId,
                                               AccountingApi.Money value, String ruleId, LocalDate date) {
        Validate.notNull(ruleId);

        final Budget budget = findSecurely(new BudgetRuleId(ruleId));
        Validate.notNull(budget, "Budget not found");

        BudgetPlan item = new BudgetPlan(
                BudgetPlanId.nextId(),
                budgetRepository.findRule(new BudgetRuleId(ruleId)),
                date,
                BudgetDirection.of(direction),
                value == null ? null : ru.vzotov.domain.model.Money.ofRaw(value.amount(), Currency.getInstance(value.currency())),
                sourceAccount == null ? null : new AccountNumber(sourceAccount),
                targetAccount == null ? null : new AccountNumber(targetAccount),
                categoryId == null ? null : new BudgetCategoryId(categoryId),
                purchaseCategoryId == null ? null : new PurchaseCategoryId(purchaseCategoryId)
        );
        budgetPlanRepository.store(item);
        return BudgetPlanDTOAssembler.toDTO(item);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountingApi.BudgetPlan modifyPlan(String itemId, String direction, String sourceAccount, String targetAccount,
                                               Long categoryId, String purchaseCategoryId,
                                               AccountingApi.Money value, String ruleId, LocalDate date) throws BudgetPlanNotFoundException {
        final Budget ruleBudget = findSecurely(new BudgetRuleId(ruleId));
        Validate.notNull(ruleBudget, "Budget for rule not found");

        BudgetPlan item = budgetPlanRepository.find(new BudgetPlanId(itemId));
        if (item == null) throw new BudgetPlanNotFoundException();

        final Budget itemBudget = findSecurely(item.rule().ruleId());
        Validate.notNull(itemBudget, "Budget for plan not found");

        item = item.withData(
                budgetRepository.findRule(new BudgetRuleId(ruleId)),
                date,
                BudgetDirection.of(direction),
                value == null ? null : ru.vzotov.domain.model.Money.ofRaw(value.amount(), Currency.getInstance(value.currency())),
                sourceAccount == null ? null : new AccountNumber(sourceAccount),
                targetAccount == null ? null : new AccountNumber(targetAccount),
                categoryId == null ? null : new BudgetCategoryId(categoryId),
                purchaseCategoryId == null ? null : new PurchaseCategoryId(purchaseCategoryId)
        );
        budgetPlanRepository.store(item);
        return BudgetPlanDTOAssembler.toDTO(item);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountingApi.BudgetPlan deletePlan(BudgetPlanId itemId) {
        final BudgetPlan item = budgetPlanRepository.find(itemId);
        if (item == null) return null;

        final Budget budget = findSecurely(item.rule().ruleId());
        Validate.notNull(budget, "Budget not found");

        budgetPlanRepository.delete(itemId);
        return BudgetPlanDTOAssembler.toDTO(item);
    }

    BudgetRule createRuleFromDTO(AccountingApi.BudgetRule rule) {
        return new BudgetRule(
                rule.ruleId() == null ? null : new BudgetRuleId(rule.ruleId()),
                BudgetRuleType.of(rule.ruleType().charAt(0)),
                rule.categoryId() == null ? null : new BudgetCategoryId(rule.categoryId()),
                rule.purchaseCategoryId() == null ? null : new PurchaseCategoryId(rule.purchaseCategoryId()),
                rule.sourceAccount() == null ? null : new AccountNumber(rule.sourceAccount()),
                rule.targetAccount() == null ? null : new AccountNumber(rule.targetAccount()),
                rule.recurrence() == null ? null : Recurrence.fromString(rule.recurrence()),
                rule.name(),
                rule.value() == null ? null : ru.vzotov.domain.model.Money.ofRaw(rule.value().amount(), Currency.getInstance(rule.value().currency())),
                rule.calculation() == null ? null : new Calculation(rule.calculation()),
                rule.enabled() == null || rule.enabled()
        );
    }
}
