package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public class RemainDTO {
    private String remainId;
    private LocalDate date;
    private String accountNumber;
    private MoneyDTO value;

    public RemainDTO() {
    }

    public RemainDTO(String remainId, LocalDate date, String accountNumber, MoneyDTO value) {
        this.remainId = remainId;
        this.date = date;
        this.accountNumber = accountNumber;
        this.value = value;
    }

    public String getRemainId() {
        return remainId;
    }

    public void setRemainId(String remainId) {
        this.remainId = remainId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public MoneyDTO getValue() {
        return value;
    }

    public void setValue(MoneyDTO value) {
        this.value = value;
    }
}
