package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import jakarta.persistence.AttributeConverter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
        return (localDateTime == null ? null : Timestamp.valueOf(localDateTime));
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
