package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public record CardOperationDTO(String operationId, String cardNumber, PosTerminalDTO terminal, LocalDate authDate,
                               LocalDate purchaseDate, MoneyDTO amount, String extraInfo, String mcc) implements OperationRef {
}
