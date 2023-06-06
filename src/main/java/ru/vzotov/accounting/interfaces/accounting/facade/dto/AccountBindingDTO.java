package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public record AccountBindingDTO(String accountNumber, LocalDate from, LocalDate to) {

}
