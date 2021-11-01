package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import ru.vzotov.accounting.domain.model.BudgetDirection;

import javax.persistence.AttributeConverter;

public class BudgetDirectionAttributeConverter implements AttributeConverter<BudgetDirection, String> {
    @Override
    public String convertToDatabaseColumn(BudgetDirection direction) {
        return direction == null ? null : String.valueOf(direction.symbol());
    }

    @Override
    public BudgetDirection convertToEntityAttribute(String value) {
        return value == null || value.length() != 1 ? null : BudgetDirection.of(value.charAt(0));
    }
}
