package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public record TimePeriodDTO(LocalDate fromDay, LocalDate toDay, long count) {

}
