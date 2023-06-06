package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public record HoldOperationDTO(String id, String account, LocalDate date, String operationType, double amount,
                               String currency, String description, String comment) {
}
