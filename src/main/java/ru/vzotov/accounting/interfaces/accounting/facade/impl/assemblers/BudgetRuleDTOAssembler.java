package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetRuleDTO;
import ru.vzotov.accounting.domain.model.BudgetRule;

public class BudgetRuleDTOAssembler {

    public static BudgetRuleDTO toDTO(BudgetRule rule) {
        return rule == null ? null : new BudgetRuleDTO(
                rule.ruleId().value(),
                String.valueOf(rule.type().symbol()),
                rule.name(),
                rule.categoryId() == null ? null : rule.categoryId().id(),
                rule.sourceAccount() == null ? null : rule.sourceAccount().number(),
                rule.targetAccount() == null ? null : rule.targetAccount().number(),
                rule.recurrence() == null ? null : rule.recurrence().toString(),
                MoneyDTOAssembler.toDTO(rule.value()),
                rule.calculation() == null ? null : rule.calculation().expression(),
                rule.isEnabled()
        );
    }
}
