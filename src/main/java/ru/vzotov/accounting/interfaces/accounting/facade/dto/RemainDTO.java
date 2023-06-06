package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public record RemainDTO(String remainId, LocalDate date, String accountNumber, MoneyDTO value) {
}
