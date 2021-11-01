package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import ru.vzotov.banking.domain.model.OperationType;

import javax.persistence.AttributeConverter;

public class OperationTypeAttributeConverter implements AttributeConverter<OperationType, String> {
    @Override
    public String convertToDatabaseColumn(OperationType type) {
        return type == null ? null : String.valueOf(type.symbol());
    }

    @Override
    public OperationType convertToEntityAttribute(String value) {
        return value == null || value.length() != 1 ? null : OperationType.of(value.charAt(0));
    }
}
