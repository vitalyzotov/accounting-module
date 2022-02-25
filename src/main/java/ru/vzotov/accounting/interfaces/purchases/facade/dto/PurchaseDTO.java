package ru.vzotov.accounting.interfaces.purchases.facade.dto;

import ru.vzotov.accounting.interfaces.common.dto.MoneyDTO;

import java.time.LocalDateTime;

public class PurchaseDTO extends PurchaseRef {


    private String receiptId;

    private String owner;

    private String name;

    private LocalDateTime dateTime;

    private MoneyDTO price;

    private Double quantity;

    private String categoryId;

    public PurchaseDTO() {
    }

    public PurchaseDTO(String purchaseId, String owner, String receiptId, String name, LocalDateTime dateTime, MoneyDTO price, Double quantity, String categoryId) {
        super(purchaseId);
        this.receiptId = receiptId;
        this.owner = owner;
        this.name = name;
        this.dateTime = dateTime;
        this.price = price;
        this.quantity = quantity;
        this.categoryId = categoryId;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
