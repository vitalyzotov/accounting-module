package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public class AccountBindingDTO {

    private String accountNumber;

    private LocalDate from;

    private LocalDate to;

    public AccountBindingDTO() {
    }

    public AccountBindingDTO(String accountNumber, LocalDate from, LocalDate to) {
        this.accountNumber = accountNumber;
        this.from = from;
        this.to = to;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }
}
