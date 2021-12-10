package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import ru.vzotov.cashreceipt.domain.model.CheckState;

import javax.persistence.AttributeConverter;

public class ReceiptStateAttributeConverter implements AttributeConverter<CheckState, String> {
    @Override
    public String convertToDatabaseColumn(CheckState attribute) {
        return attribute == null ? null : String.valueOf(attribute.symbol());
    }

    @Override
    public CheckState convertToEntityAttribute(String dbData) {
        return dbData == null || dbData.length() != 1 ? null : CheckState.of(dbData.charAt(0));
    }
}
