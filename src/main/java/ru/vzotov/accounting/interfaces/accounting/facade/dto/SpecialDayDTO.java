package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public record SpecialDayDTO(LocalDate day, String name, String type) {
}
