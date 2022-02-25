package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetDTO;
import ru.vzotov.accounting.domain.model.Budget;
import ru.vzotov.accounting.domain.model.BudgetRule;

import java.util.Comparator;
import java.util.stream.Collectors;

public class BudgetDTOAssembler {

    public static BudgetDTO toDTO(Budget budget) {
        return budget == null ? null : new BudgetDTO(
                budget.budgetId().value(),
                budget.owner().value(),
                budget.name(),
                budget.locale(),
                budget.currency() == null ? null : budget.currency().getCurrencyCode(),
                budget.rules() == null ? null :
                        budget.rules().stream()
                                .sorted(Comparator.comparing(BudgetRule::name))
                                .map(BudgetRuleDTOAssembler::toDTO)
                                .collect(Collectors.toList())
        );
    }
}
