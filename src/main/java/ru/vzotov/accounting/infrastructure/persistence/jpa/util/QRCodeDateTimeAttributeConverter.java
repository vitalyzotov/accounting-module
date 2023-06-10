package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import ru.vzotov.cashreceipt.domain.model.QRCodeDateTime;

import jakarta.persistence.AttributeConverter;
import java.sql.Timestamp;

public class QRCodeDateTimeAttributeConverter implements AttributeConverter<QRCodeDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(QRCodeDateTime localDateTime) {
        return (localDateTime == null ? null : Timestamp.valueOf(localDateTime.value()));
    }

    @Override
    public QRCodeDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
        return (sqlTimestamp == null ? null : new QRCodeDateTime(sqlTimestamp.toLocalDateTime()));
    }
}
