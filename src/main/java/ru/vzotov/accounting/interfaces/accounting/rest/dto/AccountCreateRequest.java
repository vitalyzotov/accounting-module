package ru.vzotov.accounting.interfaces.accounting.rest.dto;

import java.util.List;

public class AccountCreateRequest {

    private String number;
    private String name;
    private String bankId;
    private String currency;
    private String owner;
    private List<String> aliases;

    public AccountCreateRequest() {
    }

    public AccountCreateRequest(String number, String name, String bankId, String currency, String owner, List<String> aliases) {
        this.number = number;
        this.name = name;
        this.bankId = bankId;
        this.currency = currency;
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

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
