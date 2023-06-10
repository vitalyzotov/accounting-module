package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import ru.vzotov.calendar.domain.model.DayType;

import jakarta.persistence.AttributeConverter;

public class DayTypeAttributeConverter implements AttributeConverter<DayType, String> {
    @Override
    public String convertToDatabaseColumn(DayType type) {
        return type == null ? null : String.valueOf(type.symbol());
    }

    @Override
    public DayType convertToEntityAttribute(String value) {
        return value == null || value.length() != 1 ? null : DayType.of(value.charAt(0));
    }
}
