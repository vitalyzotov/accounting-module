package ru.vzotov.accounting.interfaces.accounting.rest.dto;

import java.util.List;

public class AccountModifyRequest {

    private String name;

    private String bankId;

    private String currency;

    private List<String> aliases;

    public AccountModifyRequest() {
    }

    public AccountModifyRequest(String name, String bankId, String currency, List<String> aliases) {
        this.name = name;
        this.bankId = bankId;
        this.currency = currency;
        this.aliases = aliases;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
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
