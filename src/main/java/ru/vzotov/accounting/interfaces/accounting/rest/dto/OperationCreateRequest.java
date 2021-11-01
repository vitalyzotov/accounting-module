package ru.vzotov.accounting.interfaces.accounting.rest.dto;

import java.time.LocalDate;

public class OperationCreateRequest {

    //private String account; // from request
    private LocalDate date;
    private LocalDate authorizationDate; // not used
    private String transactionReference;
    //private String operationId; // generated
    private String operationType;
    private double amount;
    private String currency;
    private String description;
    private String comment;
    private Long categoryId;

    public OperationCreateRequest() {
    }

    public OperationCreateRequest(LocalDate date, LocalDate authorizationDate, String transactionReference, String operationType, double amount, String currency, String description, String comment, Long categoryId) {
        this.date = date;
        this.authorizationDate = authorizationDate;
        this.transactionReference = transactionReference;
        this.operationType = operationType;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.comment = comment;
        this.categoryId = categoryId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getAuthorizationDate() {
        return authorizationDate;
    }

    public void setAuthorizationDate(LocalDate authorizationDate) {
        this.authorizationDate = authorizationDate;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
