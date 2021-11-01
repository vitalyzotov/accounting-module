package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import ru.vzotov.accounting.domain.model.BudgetPlanRepository;
import ru.vzotov.accounting.domain.model.BudgetRepository;
import ru.vzotov.accounting.interfaces.accounting.facade.BudgetFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetPlanDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetPlanNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetRuleDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.MoneyDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.BudgetDTOAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.BudgetPlanDTOAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.BudgetRuleDTOAssembler;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.domain.model.Budget;
import ru.vzotov.accounting.domain.model.BudgetDirection;
import ru.vzotov.accounting.domain.model.BudgetId;
import ru.vzotov.accounting.domain.model.BudgetPlan;
import ru.vzotov.accounting.domain.model.BudgetPlanId;
import ru.vzotov.accounting.domain.model.BudgetRule;
import ru.vzotov.accounting.domain.model.BudgetRuleId;
import ru.vzotov.accounting.domain.model.BudgetRuleType;
import ru.vzotov.accounting.domain.model.Calculation;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.BudgetCategoryId;
import ru.vzotov.calendar.domain.model.Recurrence;
import ru.vzotov.domain.model.Money;

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
    public List<BudgetDTO> listBudgets() {
        return budgetRepository.findAll().stream().map(BudgetDTOAssembler::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public BudgetDTO getBudget(BudgetId budgetId) {
        return BudgetDTOAssembler.toDTO(budgetRepository.find(budgetId));
    }

    @Override
    @Transactional(value = "accounting-tx")
    public BudgetDTO createBudget(BudgetId budgetId, String name, String currency, String locale) {
        Budget budget = new Budget(budgetId, name, new HashSet<>(), Currency.getInstance(currency), locale);
        budgetRepository.store(budget);
        return BudgetDTOAssembler.toDTO(budget);
    }

    @Override
    @Transactional(value = "accounting-tx")
    public BudgetDTO modifyBudget(BudgetId budgetId, String name, String currency, String locale) throws BudgetNotFoundException {
        Budget budget = budgetRepository.find(budgetId);
        if (budget == null) throw new BudgetNotFoundException();
        budget.setName(name);
        budget.setCurrency(Currency.getInstance(currency));
        budget.setLocale(locale);
        budgetRepository.store(budget);
        return BudgetDTOAssembler.toDTO(budget);
    }

    @Override
    @Transactional(value = "accounting-tx")
    public BudgetDTO deleteBudget(BudgetId budgetId) throws BudgetNotFoundException {
        Budget budget = budgetRepository.find(budgetId);
        if (budget == null) throw new BudgetNotFoundException();
        return budgetRepository.delete(budgetId) ? BudgetDTOAssembler.toDTO(budget) : null;
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public BudgetRuleDTO getBudgetRule(BudgetId budgetId, String ruleId) throws BudgetNotFoundException {
        Validate.notNull(budgetId);
        Validate.notNull(ruleId);

        Budget budget = budgetRepository.find(budgetId);
        if (budget == null) throw new BudgetNotFoundException();

        return budget.rules().stream()
                .filter(r -> ruleId.equals(r.ruleId().value()))
                .map(BudgetRuleDTOAssembler::toDTO)
                .findFirst()
                .orElse(null);
    }

    @Override
    @Transactional(value = "accounting-tx")
    public BudgetDTO addRuleToBudget(BudgetId budgetId, BudgetRuleDTO rule) throws BudgetNotFoundException {
        Budget budget = budgetRepository.find(budgetId);
        if (budget == null) throw new BudgetNotFoundException();
        BudgetRule r = createRuleFromDTO(rule);
        budget.addRule(r);
        budgetRepository.store(budget);
        return BudgetDTOAssembler.toDTO(budgetRepository.find(budgetId));
    }

    @Override
    @Transactional(value = "accounting-tx")
    public BudgetDTO deleteRuleFromBudget(BudgetId budgetId, String ruleId) throws BudgetNotFoundException {
        Validate.notNull(budgetId);
        Validate.notNull(ruleId);

        Budget budget = budgetRepository.find(budgetId);
        if (budget == null) throw new BudgetNotFoundException();

        budget.rules().stream()
                .filter(r -> ruleId.equals(r.ruleId().value()))
                .collect(Collectors.toList())
                .forEach(budget::deleteRule);

        budgetRepository.store(budget);
        return BudgetDTOAssembler.toDTO(budgetRepository.find(budgetId));
    }

    @Override
    @Transactional(value = "accounting-tx")
    public BudgetDTO replaceBudgetRule(BudgetId budgetId, String ruleId, BudgetRuleDTO rule) throws BudgetNotFoundException {
        Budget budget = budgetRepository.find(budgetId);
        if (budget == null) throw new BudgetNotFoundException();

        budget.rules().stream()
                .filter(r -> ruleId.equals(r.ruleId().value()))
                .collect(Collectors.toSet())
                .forEach(budget::deleteRule);
        budgetRepository.store(budget);

        BudgetRule r = createRuleFromDTO(rule);
        budget.addRule(r);

        budgetRepository.store(budget);
        return BudgetDTOAssembler.toDTO(budgetRepository.find(budgetId));
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public List<BudgetPlanDTO> listPlans(BudgetId budgetId) {
        final Budget budget = budgetRepository.find(budgetId);
        return budget.rules().stream()
                .map(BudgetRule::ruleId)
                .flatMap(ruleId -> budgetPlanRepository.findForRule(ruleId).stream())
                .map(BudgetPlanDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public BudgetPlanDTO getPlan(BudgetPlanId itemId) {
        Validate.notNull(itemId);
        return BudgetPlanDTOAssembler.toDTO(budgetPlanRepository.find(itemId));
    }

    @Override
    @Transactional(value = "accounting-tx")
    public BudgetPlanDTO createPlan(String direction, String sourceAccount, String targetAccount, Long categoryId, MoneyDTO value, String ruleId, LocalDate date) {
        BudgetPlan item = new BudgetPlan(
                BudgetPlanId.nextId(),
                budgetRepository.findRule(new BudgetRuleId(ruleId)),
                date,
                BudgetDirection.of(direction),
                value == null ? null : Money.ofRaw(value.getAmount(), Currency.getInstance(value.getCurrency())),
                sourceAccount == null ? null : new AccountNumber(sourceAccount),
                targetAccount == null ? null : new AccountNumber(targetAccount),
                categoryId == null ? null : new BudgetCategoryId(categoryId)
        );
        budgetPlanRepository.store(item);
        return BudgetPlanDTOAssembler.toDTO(item);
    }

    @Override
    @Transactional(value = "accounting-tx")
    public BudgetPlanDTO modifyPlan(String itemId, String direction, String sourceAccount, String targetAccount, Long categoryId, MoneyDTO value, String ruleId, LocalDate date) throws BudgetPlanNotFoundException {
        BudgetPlan item = budgetPlanRepository.find(new BudgetPlanId(itemId));
        if (item == null) throw new BudgetPlanNotFoundException();
        item = item.withData(budgetRepository.findRule(new BudgetRuleId(ruleId)),
                date,
                BudgetDirection.of(direction),
                value == null ? null : Money.ofRaw(value.getAmount(), Currency.getInstance(value.getCurrency())),
                sourceAccount == null ? null : new AccountNumber(sourceAccount),
                targetAccount == null ? null : new AccountNumber(targetAccount),
                categoryId == null ? null : new BudgetCategoryId(categoryId));
        budgetPlanRepository.store(item);
        return BudgetPlanDTOAssembler.toDTO(item);
    }

    @Override
    @Transactional(value = "accounting-tx")
    public BudgetPlanDTO deletePlan(BudgetPlanId itemId) {
        final BudgetPlan item = budgetPlanRepository.find(itemId);
        if (item == null) return null;
        budgetPlanRepository.delete(itemId);
        return BudgetPlanDTOAssembler.toDTO(item);
    }

    BudgetRule createRuleFromDTO(BudgetRuleDTO rule) {
        return new BudgetRule(
                rule.getRuleId() == null ? null : new BudgetRuleId(rule.getRuleId()),
                BudgetRuleType.of(rule.getRuleType().charAt(0)),
                rule.getCategoryId() == null ? null : new BudgetCategoryId(rule.getCategoryId()),
                rule.getSourceAccount() == null ? null : new AccountNumber(rule.getSourceAccount()),
                rule.getTargetAccount() == null ? null : new AccountNumber(rule.getTargetAccount()),
                rule.getRecurrence() == null ? null : Recurrence.fromString(rule.getRecurrence()),
                rule.getName(),
                rule.getValue() == null ? null : Money.ofRaw(rule.getValue().getAmount(), Currency.getInstance(rule.getValue().getCurrency())),
                rule.getCalculation() == null ? null : new Calculation(rule.getCalculation()),
                rule.getEnabled() == null ? true : rule.getEnabled()
        );
    }
}
