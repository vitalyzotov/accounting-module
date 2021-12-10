package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import ru.vzotov.cashreceipt.domain.model.CheckOperationType;

import javax.persistence.AttributeConverter;

public class ReceiptOperationTypeAttributeConverter implements AttributeConverter<CheckOperationType, String> {
    @Override
    public String convertToDatabaseColumn(CheckOperationType attribute) {
        return attribute == null ? null : String.valueOf(attribute.numericValue());
    }

    @Override
    public CheckOperationType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CheckOperationType.of(Long.parseLong(dbData));
    }
}
