package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.beans.factory.annotation.Autowired;
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
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.accounting.facade.BudgetFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetPlanNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/accounting/budget")
@CrossOrigin
public class BudgetController {

    @Autowired
    private BudgetFacade budgetFacade;

    @GetMapping("{budgetId}")
    public AccountingApi.Budget getBudget(@PathVariable String budgetId) {
        return budgetFacade.getBudget(new BudgetId(budgetId));
    }

    @DeleteMapping("{budgetId}")
    public AccountingApi.Budget deleteBudget(@PathVariable String budgetId) throws BudgetNotFoundException {
        return budgetFacade.deleteBudget(new BudgetId(budgetId));
    }

    @GetMapping
    public List<AccountingApi.Budget> listBudgets() {
        return budgetFacade.listBudgets();
    }

    @PostMapping
    public AccountingApi.Budget createBudget(@RequestBody AccountingApi.BudgetCreateRequest request) {
        return budgetFacade.createBudget(BudgetId.nextId(), request.name(), request.currency(), request.locale());
    }

    @PutMapping("{budgetId}")
    public AccountingApi.Budget modifyBudget(@PathVariable String budgetId, @RequestBody AccountingApi.BudgetModifyRequest request) throws BudgetNotFoundException {
        return budgetFacade.modifyBudget(new BudgetId(budgetId), request.name(), request.currency(), request.locale());
    }

    @GetMapping("{budgetId}/rule/{ruleId}")
    public AccountingApi.BudgetRule getRule(@PathVariable String budgetId, @PathVariable String ruleId) throws BudgetNotFoundException {
        return budgetFacade.getBudgetRule(new BudgetId(budgetId), ruleId);
    }

    @PostMapping("{budgetId}/rule")
    public AccountingApi.Budget addRule(@PathVariable String budgetId, @RequestBody AccountingApi.BudgetRule rule) throws BudgetNotFoundException {
        return budgetFacade.addRuleToBudget(new BudgetId(budgetId), rule);
    }

    @DeleteMapping("{budgetId}/rule/{ruleId}")
    public AccountingApi.Budget deleteRule(@PathVariable String budgetId, @PathVariable String ruleId) throws BudgetNotFoundException {
        return budgetFacade.deleteRuleFromBudget(new BudgetId(budgetId), ruleId);
    }

    @PutMapping("{budgetId}/rule/{ruleId}")
    public AccountingApi.Budget replaceRule(@PathVariable String budgetId, @PathVariable String ruleId, @RequestBody AccountingApi.BudgetRule rule) throws BudgetNotFoundException {
        return budgetFacade.replaceBudgetRule(new BudgetId(budgetId), ruleId, rule);
    }

    @GetMapping("{budgetId}/plan")
    public List<AccountingApi.BudgetPlan> listPlans(@PathVariable String budgetId) {
        return budgetFacade.listPlans(new BudgetId(budgetId));
    }

    @GetMapping("{budgetId}/plan/{itemId}")
    public AccountingApi.BudgetPlan getPlan(@PathVariable String budgetId, @PathVariable String itemId) {
        return budgetFacade.getPlan(new BudgetPlanId(itemId));
    }

    @PostMapping("{budgetId}/plan")
    public AccountingApi.BudgetPlan createPlan(@PathVariable String budgetId, @RequestBody AccountingApi.BudgetPlan item) {
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
    public AccountingApi.BudgetPlan modifyPlan(@PathVariable String budgetId, @PathVariable String itemId,
                                               @RequestBody AccountingApi.BudgetPlan item) throws BudgetPlanNotFoundException {
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
    public AccountingApi.BudgetPlan deletePlan(@PathVariable String budgetId, @PathVariable String itemId) {
        return budgetFacade.deletePlan(new BudgetPlanId(itemId));
    }
}
