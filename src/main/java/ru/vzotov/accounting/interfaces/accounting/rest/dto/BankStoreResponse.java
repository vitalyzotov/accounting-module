package ru.vzotov.accounting.interfaces.accounting.rest.dto;

public class BankStoreResponse {
    private String bankId;

    public BankStoreResponse() {
    }

    public BankStoreResponse(String bankId) {
        this.bankId = bankId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
}
