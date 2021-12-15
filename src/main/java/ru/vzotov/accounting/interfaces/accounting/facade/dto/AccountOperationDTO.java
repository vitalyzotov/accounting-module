package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public class AccountOperationDTO extends OperationRef {

    private String account;
    private LocalDate date;
    private LocalDate authorizationDate;
    private String transactionReference;
    private String operationType;
    private double amount;
    private String currency;
    private String description;
    private String comment;
    private Long categoryId;

    public AccountOperationDTO() {
    }

    public AccountOperationDTO(String account, LocalDate date, LocalDate authorizationDate, String transactionReference,
                               String operationId, String operationType, double amount, String currency,
                               String description, String comment, Long categoryId) {
        super(operationId);
        this.account = account;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
}
