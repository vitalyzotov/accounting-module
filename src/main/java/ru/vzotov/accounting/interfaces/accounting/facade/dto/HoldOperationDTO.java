package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public class HoldOperationDTO {
    private String id;
    private String account;
    private LocalDate date;
    private String operationType;
    private double amount;
    private String currency;
    private String description;
    private String comment;

    public HoldOperationDTO() {
    }

    public HoldOperationDTO(String id, String account, LocalDate date, String operationType, double amount, String currency,
                            String description, String comment) {
        this.id = id;
        this.account = account;
        this.date = date;
        this.operationType = operationType;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
