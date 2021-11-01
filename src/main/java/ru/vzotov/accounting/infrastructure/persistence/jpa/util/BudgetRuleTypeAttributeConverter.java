package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import ru.vzotov.accounting.domain.model.BudgetRuleType;

import javax.persistence.AttributeConverter;

public class BudgetRuleTypeAttributeConverter implements AttributeConverter<BudgetRuleType, String> {
    @Override
    public String convertToDatabaseColumn(BudgetRuleType type) {
        return type == null ? null : String.valueOf(type.symbol());
    }

    @Override
    public BudgetRuleType convertToEntityAttribute(String value) {
        return value == null || value.length() != 1 ? null : BudgetRuleType.of(value.charAt(0));
    }
}
