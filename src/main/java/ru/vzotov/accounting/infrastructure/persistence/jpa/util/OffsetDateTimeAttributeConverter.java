package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import jakarta.persistence.AttributeConverter;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class OffsetDateTimeAttributeConverter implements AttributeConverter<OffsetDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(OffsetDateTime offsetDateTime) {
        return (offsetDateTime == null ? null : Timestamp.from(offsetDateTime.toInstant()));
    }

    @Override
    public OffsetDateTime convertToEntityAttribute(Timestamp timestamp) {
        return timestamp == null ? null : OffsetDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.UTC);
    }
}
