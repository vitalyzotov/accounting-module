package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.WorkCalendarDTO;

import java.time.LocalDate;

public interface WorkCalendarFacade {
    WorkCalendarDTO getCalendar(String location, LocalDate from, LocalDate to);
}
