package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import ru.vzotov.calendar.domain.model.Recurrence;

import javax.persistence.AttributeConverter;

public class RecurrenceAttributeConverter implements AttributeConverter<Recurrence, String> {
    @Override
    public String convertToDatabaseColumn(Recurrence type) {
        return type == null ? null : type.toString();
    }

    @Override
    public Recurrence convertToEntityAttribute(String value) {
        return value == null ? null : Recurrence.fromString(value);
    }
}
