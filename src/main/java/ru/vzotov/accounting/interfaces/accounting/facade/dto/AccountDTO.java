package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.io.Serializable;
import java.util.List;

public final class AccountDTO implements Serializable {
    private String number;
    private String name;
    private String currency;
    private String bankId;
    private String owner;
    private List<String> aliases;

    public AccountDTO() {
    }

    public AccountDTO(String number, String name, String bankId, String currency, String owner, List<String> aliases) {
        this.number = number;
        this.name = name;
        this.currency = currency;
        this.bankId = bankId;
        this.owner = owner;
        this.aliases = aliases;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public String getBankId() {
        return bankId;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
}
