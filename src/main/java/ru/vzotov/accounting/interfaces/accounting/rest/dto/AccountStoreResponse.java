package ru.vzotov.accounting.interfaces.accounting.rest.dto;

public class AccountStoreResponse {
    private String accountNumber;

    public AccountStoreResponse() {
    }

    public AccountStoreResponse(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
