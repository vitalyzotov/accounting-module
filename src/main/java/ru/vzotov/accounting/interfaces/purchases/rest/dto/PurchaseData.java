package ru.vzotov.accounting.interfaces.purchases.rest.dto;

import ru.vzotov.accounting.interfaces.common.dto.MoneyDTO;

import java.time.LocalDateTime;

public class PurchaseData extends PurchaseDataRequest {

    public PurchaseData() {
    }

    public PurchaseData(String name, LocalDateTime dateTime, MoneyDTO price, Double quantity, String receiptId, String categoryId) {
        super(name, dateTime, price, quantity, receiptId, categoryId);
    }
}
