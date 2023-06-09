package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.domain.model.BudgetPlan;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;

public class BudgetPlanDTOAssembler {

    public static AccountingApi.BudgetPlan toDTO(BudgetPlan item) {
        return item == null ? null : new AccountingApi.BudgetPlan(
                item.itemId().value(),
                item.direction() == null ? null : String.valueOf(item.direction().symbol()),
                item.source() == null ? null : item.source().number(),
                item.target() == null ? null : item.target().number(),
                item.category() == null ? null : item.category().id(),
                item.purchaseCategory() == null ? null : item.purchaseCategory().value(),
                MoneyDTOAssembler.toDTO(item.value()),
                item.rule() == null ? null : item.rule().ruleId().value(),
                item.date()
        );
    }
}
