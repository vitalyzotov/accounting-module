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
import ru.vzotov.accounting.interfaces.accounting.facade.BudgetFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetPlanDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetPlanNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetRuleDTO;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.BudgetCreateRequest;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.BudgetModifyRequest;

import java.util.List;

@RestController
@RequestMapping("/accounting/budget")
@CrossOrigin
public class BudgetController {

    @Autowired
    private BudgetFacade budgetFacade;

    @GetMapping("{budgetId}")
    public BudgetDTO getBudget(@PathVariable String budgetId) {
        return budgetFacade.getBudget(new BudgetId(budgetId));
    }

    @DeleteMapping("{budgetId}")
    public BudgetDTO deleteBudget(@PathVariable String budgetId) throws BudgetNotFoundException {
        return budgetFacade.deleteBudget(new BudgetId(budgetId));
    }

    @GetMapping
    public List<BudgetDTO> listBudgets() {
        return budgetFacade.listBudgets();
    }

    @PostMapping
    public BudgetDTO createBudget(@RequestBody BudgetCreateRequest request) {
        return budgetFacade.createBudget(BudgetId.nextId(), request.getName(), request.getCurrency(), request.getLocale());
    }

    @PutMapping("{budgetId}")
    public BudgetDTO modifyBudget(@PathVariable String budgetId, @RequestBody BudgetModifyRequest request) throws BudgetNotFoundException {
        return budgetFacade.modifyBudget(new BudgetId(budgetId), request.getName(), request.getCurrency(), request.getLocale());
    }

    @GetMapping("{budgetId}/rule/{ruleId}")
    public BudgetRuleDTO getRule(@PathVariable String budgetId, @PathVariable String ruleId) throws BudgetNotFoundException {
        return budgetFacade.getBudgetRule(new BudgetId(budgetId), ruleId);
    }

    @PostMapping("{budgetId}/rule")
    public BudgetDTO addRule(@PathVariable String budgetId, @RequestBody BudgetRuleDTO rule) throws BudgetNotFoundException {
        return budgetFacade.addRuleToBudget(new BudgetId(budgetId), rule);
    }

    @DeleteMapping("{budgetId}/rule/{ruleId}")
    public BudgetDTO deleteRule(@PathVariable String budgetId, @PathVariable String ruleId) throws BudgetNotFoundException {
        return budgetFacade.deleteRuleFromBudget(new BudgetId(budgetId), ruleId);
    }

    @PutMapping("{budgetId}/rule/{ruleId}")
    public BudgetDTO replaceRule(@PathVariable String budgetId, @PathVariable String ruleId, @RequestBody BudgetRuleDTO rule) throws BudgetNotFoundException {
        return budgetFacade.replaceBudgetRule(new BudgetId(budgetId), ruleId, rule);
    }

    @GetMapping("{budgetId}/plan")
    public List<BudgetPlanDTO> listPlans(@PathVariable String budgetId) {
        return budgetFacade.listPlans(new BudgetId(budgetId));
    }

    @GetMapping("{budgetId}/plan/{itemId}")
    public BudgetPlanDTO getPlan(@PathVariable String budgetId, @PathVariable String itemId) {
        return budgetFacade.getPlan(new BudgetPlanId(itemId));
    }

    @PostMapping("{budgetId}/plan")
    public BudgetPlanDTO createPlan(@PathVariable String budgetId, @RequestBody BudgetPlanDTO item) {
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
    public BudgetPlanDTO modifyPlan(@PathVariable String budgetId, @PathVariable String itemId,
                                    @RequestBody BudgetPlanDTO item) throws BudgetPlanNotFoundException {
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
    public BudgetPlanDTO deletePlan(@PathVariable String budgetId, @PathVariable String itemId) {
        return budgetFacade.deletePlan(new BudgetPlanId(itemId));
    }
}
