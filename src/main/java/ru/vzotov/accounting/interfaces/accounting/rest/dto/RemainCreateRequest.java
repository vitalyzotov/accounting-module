package ru.vzotov.accounting.interfaces.accounting.rest.dto;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.MoneyDTO;

import java.time.LocalDate;

public class RemainCreateRequest {
    private String accountNumber;
    private LocalDate date;
    private MoneyDTO value;

    public RemainCreateRequest() {
    }

    public RemainCreateRequest(String accountNumber, LocalDate date, MoneyDTO value) {
        this.accountNumber = accountNumber;
        this.date = date;
        this.value = value;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public MoneyDTO getValue() {
        return value;
    }

    public void setValue(MoneyDTO value) {
        this.value = value;
    }
}
