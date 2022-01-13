package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import ru.vzotov.cashreceipt.domain.model.ReceiptOperationType;

import javax.persistence.AttributeConverter;

public class ReceiptOperationTypeAttributeConverter implements AttributeConverter<ReceiptOperationType, String> {
    @Override
    public String convertToDatabaseColumn(ReceiptOperationType attribute) {
        return attribute == null ? null : String.valueOf(attribute.numericValue());
    }

    @Override
    public ReceiptOperationType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ReceiptOperationType.of(Long.parseLong(dbData));
    }
}
