package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import ru.vzotov.cashreceipt.domain.model.ReceiptState;

import jakarta.persistence.AttributeConverter;

public class ReceiptStateAttributeConverter implements AttributeConverter<ReceiptState, String> {
    @Override
    public String convertToDatabaseColumn(ReceiptState attribute) {
        return attribute == null ? null : String.valueOf(attribute.symbol());
    }

    @Override
    public ReceiptState convertToEntityAttribute(String dbData) {
        return dbData == null || dbData.length() != 1 ? null : ReceiptState.of(dbData.charAt(0));
    }
}
