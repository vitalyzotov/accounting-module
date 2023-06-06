package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.util.List;

public record BudgetDTO(String budgetId, String owner, String name, String locale, String currency,
                        List<BudgetRuleDTO> rules) {
}
