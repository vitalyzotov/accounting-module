package ru.vzotov.accounting.interfaces.purchases.facade.dto;

import ru.vzotov.accounting.interfaces.common.dto.MoneyDTO;

import java.time.LocalDateTime;

public record PurchaseDTO(String purchaseId, String owner, String receiptId, String name, LocalDateTime dateTime,
                          MoneyDTO price, Double quantity, String categoryId) implements PurchaseRef {

}
