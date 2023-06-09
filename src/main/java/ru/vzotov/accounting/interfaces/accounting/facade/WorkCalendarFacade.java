package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi.WorkCalendar;

import java.time.LocalDate;

public interface WorkCalendarFacade {
    WorkCalendar getCalendar(String location, LocalDate from, LocalDate to);
}
