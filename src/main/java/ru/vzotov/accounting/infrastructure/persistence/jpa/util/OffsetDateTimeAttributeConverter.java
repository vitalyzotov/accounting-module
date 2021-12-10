package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import javax.persistence.AttributeConverter;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

// todo: move class to jpa utils
public class OffsetDateTimeAttributeConverter implements AttributeConverter<OffsetDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(OffsetDateTime offsetDateTime) {
        return (offsetDateTime == null ? null : Timestamp.valueOf(offsetDateTime.toLocalDateTime()));
    }

    @Override
    public OffsetDateTime convertToEntityAttribute(Timestamp timestamp) {
        return timestamp == null ? null : OffsetDateTime.of(timestamp.toLocalDateTime(), ZoneOffset.UTC);
    }
}
