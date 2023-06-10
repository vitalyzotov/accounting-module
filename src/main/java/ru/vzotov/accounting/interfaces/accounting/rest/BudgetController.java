package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.accounting.domain.model.BudgetId;
import ru.vzotov.accounting.domain.model.BudgetPlanId;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.Budget;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.BudgetPlan;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.BudgetRule;
import ru.vzotov.accounting.interfaces.accounting.facade.BudgetFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.BudgetNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.BudgetPlanNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/accounting/budget")
@CrossOrigin
public class BudgetController {

    private final BudgetFacade budgetFacade;

    public BudgetController(BudgetFacade budgetFacade) {
        this.budgetFacade = budgetFacade;
    }

    @GetMapping("{budgetId}")
    public Budget getBudget(@PathVariable String budgetId) {
        return budgetFacade.getBudget(new BudgetId(budgetId));
    }

    @DeleteMapping("{budgetId}")
    public Budget deleteBudget(@PathVariable String budgetId) throws BudgetNotFoundException {
        return budgetFacade.deleteBudget(new BudgetId(budgetId));
    }

    @GetMapping
    public List<Budget> listBudgets() {
        return budgetFacade.listBudgets();
    }

    @PostMapping
    public Budget createBudget(@RequestBody Budget.Create request) {
        return budgetFacade.createBudget(BudgetId.nextId(), request.name(), request.currency(), request.locale());
    }

    @PutMapping("{budgetId}")
    public Budget modifyBudget(@PathVariable String budgetId, @RequestBody Budget.Modify request)
            throws BudgetNotFoundException {
        return budgetFacade.modifyBudget(new BudgetId(budgetId), request.name(), request.currency(), request.locale());
    }

    @GetMapping("{budgetId}/rule/{ruleId}")
    public BudgetRule getRule(@PathVariable String budgetId, @PathVariable String ruleId)
            throws BudgetNotFoundException {
        return budgetFacade.getBudgetRule(new BudgetId(budgetId), ruleId);
    }

    @PostMapping("{budgetId}/rule")
    public Budget addRule(@PathVariable String budgetId, @RequestBody BudgetRule rule)
            throws BudgetNotFoundException {
        return budgetFacade.addRuleToBudget(new BudgetId(budgetId), rule);
    }

    @DeleteMapping("{budgetId}/rule/{ruleId}")
    public Budget deleteRule(@PathVariable String budgetId, @PathVariable String ruleId)
            throws BudgetNotFoundException {
        return budgetFacade.deleteRuleFromBudget(new BudgetId(budgetId), ruleId);
    }

    @PutMapping("{budgetId}/rule/{ruleId}")
    public Budget replaceRule(@PathVariable String budgetId, @PathVariable String ruleId, @RequestBody BudgetRule rule)
            throws BudgetNotFoundException {
        return budgetFacade.replaceBudgetRule(new BudgetId(budgetId), ruleId, rule);
    }

    @GetMapping("{budgetId}/plan")
    public List<BudgetPlan> listPlans(@PathVariable String budgetId) {
        return budgetFacade.listPlans(new BudgetId(budgetId));
    }

    @GetMapping("{budgetId}/plan/{itemId}")
    public BudgetPlan getPlan(@PathVariable String budgetId, @PathVariable String itemId) {
        return budgetFacade.getPlan(new BudgetPlanId(itemId));
    }

    @PostMapping("{budgetId}/plan")
    public BudgetPlan createPlan(@PathVariable String budgetId, @RequestBody BudgetPlan item) {
        return budgetFacade.createPlan(
                item.direction(),
                item.sourceAccount(),
                item.targetAccount(),
                item.categoryId(),
                item.purchaseCategoryId(),
                item.value(),
                item.ruleId(),
                item.date()
        );
    }

    /**
     * Изменить элемент плана бюджета
     *
     * @param budgetId идентификатор бюджета
     * @param itemId   идентификатор элемента
     * @param item     данные элемента
     * @return изменный элемент
     * @throws BudgetPlanNotFoundException если элемент не найден
     */
    @PutMapping("{budgetId}/plan/{itemId}")
    public BudgetPlan modifyPlan(@PathVariable String budgetId, @PathVariable String itemId,
                                 @RequestBody BudgetPlan item) throws BudgetPlanNotFoundException {
        return budgetFacade.modifyPlan(
                itemId,
                item.direction(),
                item.sourceAccount(),
                item.targetAccount(),
                item.categoryId(),
                item.purchaseCategoryId(),
                item.value(),
                item.ruleId(),
                item.date()
        );
    }

    /**
     * Удалить элемент плана бюджета
     *
     * @param budgetId идентификатор бюджета
     * @param itemId   идентификатор элемента
     * @return удаленный элемент плана
     */
    @DeleteMapping("{budgetId}/plan/{itemId}")
    public BudgetPlan deletePlan(@PathVariable String budgetId, @PathVariable String itemId) {
        return budgetFacade.deletePlan(new BudgetPlanId(itemId));
    }

}
