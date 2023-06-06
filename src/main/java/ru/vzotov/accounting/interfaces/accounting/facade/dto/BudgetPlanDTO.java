package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public record BudgetPlanDTO(String itemId, String direction, String sourceAccount, String targetAccount,
                            Long categoryId, String purchaseCategoryId, MoneyDTO value, String ruleId, LocalDate date) {
}
