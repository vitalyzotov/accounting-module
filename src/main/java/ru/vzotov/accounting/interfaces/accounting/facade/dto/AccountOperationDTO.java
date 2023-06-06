package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public record AccountOperationDTO(String account, LocalDate date, LocalDate authorizationDate,
                                  String transactionReference, String operationId, String operationType, double amount,
                                  String currency, String description, String comment,
                                  Long categoryId) implements OperationRef {

}
