package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.domain.model.Budget;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.domain.model.BudgetRule;

import java.util.Comparator;
import java.util.stream.Collectors;

public class BudgetAssembler {

    public static AccountingApi.Budget toDTO(Budget budget) {
        return budget == null ? null : new AccountingApi.Budget(
                budget.budgetId().value(),
                budget.owner().value(),
                budget.name(),
                budget.locale(),
                budget.currency() == null ? null : budget.currency().getCurrencyCode(),
                budget.rules() == null ? null :
                        budget.rules().stream()
                                .sorted(Comparator.comparing(BudgetRule::name))
                                .map(BudgetRuleAssembler::toDTO)
                                .collect(Collectors.toList())
        );
    }
}
