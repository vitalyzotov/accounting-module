package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import javax.persistence.AttributeConverter;
import java.sql.Date;
import java.time.YearMonth;

public class YearMonthAttributeConverter implements AttributeConverter<YearMonth, Date> {
    @Override
    public Date convertToDatabaseColumn(YearMonth yearMonth) {
        return (yearMonth == null ? null : Date.valueOf(yearMonth.atEndOfMonth()));
    }

    @Override
    public YearMonth convertToEntityAttribute(Date sqlDate) {
        return (sqlDate == null ? null : YearMonth.from(sqlDate.toLocalDate()));
    }
}
