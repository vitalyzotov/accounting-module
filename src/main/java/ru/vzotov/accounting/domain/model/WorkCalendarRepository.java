package ru.vzotov.accounting.domain.model;

import ru.vzotov.calendar.domain.model.WorkCalendar;

import java.time.LocalDate;

public interface WorkCalendarRepository {
    WorkCalendar find(String location, LocalDate from, LocalDate to);
    void store(WorkCalendar calendar);
}
