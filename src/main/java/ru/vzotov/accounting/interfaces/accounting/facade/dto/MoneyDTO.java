package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoneyDTO moneyDTO = (MoneyDTO) o;

        if (amount != moneyDTO.amount) return false;
        return Objects.equals(currency, moneyDTO.currency);
    }

    @Override
    public int hashCode() {
        int result = (int) (amount ^ (amount >>> 32));
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }
}
