package ru.vzotov.accounting.interfaces.accounting.facade.dto;

public record BudgetRuleDTO(String ruleId, String ruleType, String name, Long categoryId, String purchaseCategoryId,
                            String sourceAccount, String targetAccount, String recurrence, MoneyDTO value,
                            String calculation, Boolean enabled) {
}
