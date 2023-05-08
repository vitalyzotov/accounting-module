package ru.vzotov.accounting.test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class DateUtils {

    public static OffsetDateTime inCurrentZone(LocalDateTime dateTime) {
        return OffsetDateTime.of(dateTime, ZoneOffset.systemDefault().getRules().getOffset(dateTime));
    }
}
