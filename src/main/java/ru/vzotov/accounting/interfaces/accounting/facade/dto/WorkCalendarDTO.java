package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;
import java.util.List;

public record WorkCalendarDTO(LocalDate from, LocalDate to, String location, List<SpecialDayDTO> specialDays) {

}
