package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import jakarta.persistence.AttributeConverter;
import java.time.LocalDate;
import java.sql.Date;

public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {
    @Override
    public Date convertToDatabaseColumn(LocalDate localDate) {
        return (localDate == null ? null : Date.valueOf(localDate));
    }

    @Override
    public LocalDate convertToEntityAttribute(Date sqlDate) {
        return (sqlDate == null ? null : sqlDate.toLocalDate());
    }
}
