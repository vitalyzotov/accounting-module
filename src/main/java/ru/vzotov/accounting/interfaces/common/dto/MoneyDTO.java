package ru.vzotov.accounting.interfaces.common.dto;

import java.io.Serializable;

public final class MoneyDTO implements Serializable {
    private long amount;
    private String currency;

    public MoneyDTO() {
    }

    public MoneyDTO(long amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public long getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
