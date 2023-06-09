package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.calendar.domain.model.SpecialDay;
import ru.vzotov.calendar.domain.model.WorkCalendar;

import java.util.Comparator;
import java.util.stream.Collectors;

public class WorkCalendarDTOAssembler {

    public static AccountingApi.WorkCalendar toDTO(WorkCalendar domain) {
        return domain == null ? null : new AccountingApi.WorkCalendar(
                domain.from(),
                domain.to(),
                domain.location(),
                domain.specialDays().stream()
                        .sorted(Comparator.comparing(SpecialDay::day))
                        .map(SpecialDayDTOAssembler::toDTO)
                        .collect(Collectors.toList())
        );
    }
}
