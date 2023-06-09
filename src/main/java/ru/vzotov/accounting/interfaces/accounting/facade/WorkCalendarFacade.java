package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;

import java.time.LocalDate;

public interface WorkCalendarFacade {
    AccountingApi.WorkCalendar getCalendar(String location, LocalDate from, LocalDate to);
}
