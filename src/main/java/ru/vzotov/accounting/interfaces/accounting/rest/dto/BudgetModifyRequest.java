package ru.vzotov.accounting.interfaces.accounting.rest.dto;

public class BudgetModifyRequest {
    private String name;
    private String currency;
    private String locale;

    public BudgetModifyRequest() {
    }

    public BudgetModifyRequest(String name, String currency, String locale) {
        this.name = name;
        this.currency = currency;
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
