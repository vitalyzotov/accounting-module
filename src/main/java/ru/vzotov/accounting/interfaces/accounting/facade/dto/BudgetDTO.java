package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.util.List;

public class BudgetDTO {
    private String budgetId;
    private String name;
    private List<BudgetRuleDTO> rules;
    private String locale;
    private String currency;

    public BudgetDTO() {
    }

    public BudgetDTO(String budgetId, String name, String locale, String currency, List<BudgetRuleDTO> rules) {
        this.budgetId = budgetId;
        this.name = name;
        this.rules = rules;
        this.locale = locale;
        this.currency = currency;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BudgetRuleDTO> getRules() {
        return rules;
    }

    public void setRules(List<BudgetRuleDTO> rules) {
        this.rules = rules;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
