package ru.vzotov.accounting.interfaces.purchases.rest.dto;

import ru.vzotov.accounting.interfaces.common.dto.MoneyDTO;

import java.time.LocalDateTime;

public abstract class PurchaseDataRequest {
    private String receiptId;

    private String name;

    private LocalDateTime dateTime;

    private MoneyDTO price;

    private Double quantity;

    private String categoryId;

    public PurchaseDataRequest() {
    }

    public PurchaseDataRequest(String name, LocalDateTime dateTime, MoneyDTO price, Double quantity, String receiptId, String categoryId) {
        this.name = name;
        this.dateTime = dateTime;
        this.price = price;
        this.quantity = quantity;
        this.receiptId = receiptId;
        this.categoryId = categoryId;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public MoneyDTO getPrice() {
        return price;
    }

    public void setPrice(MoneyDTO price) {
        this.price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
