package ru.vzotov.accounting.interfaces.accounting.facade.dto;

public class BudgetRuleDTO {
    private String ruleId;
    private String ruleType;
    private String name;
    private Long categoryId;
    private String purchaseCategoryId;
    private String sourceAccount;
    private String targetAccount;
    private String recurrence;
    private MoneyDTO value;
    private String calculation;
    private Boolean enabled;

    public BudgetRuleDTO() {
    }

    public BudgetRuleDTO(String ruleId, String ruleType, String name, Long categoryId, String purchaseCategoryId, String sourceAccount, String targetAccount, String recurrence, MoneyDTO value, String calculation, Boolean enabled) {
        this.ruleId = ruleId;
        this.ruleType = ruleType;
        this.name = name;
        this.categoryId = categoryId;
        this.purchaseCategoryId = purchaseCategoryId;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.recurrence = recurrence;
        this.value = value;
        this.calculation = calculation;
        this.enabled = enabled;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(String targetAccount) {
        this.targetAccount = targetAccount;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getPurchaseCategoryId() {
        return purchaseCategoryId;
    }

    public void setPurchaseCategoryId(String purchaseCategoryId) {
        this.purchaseCategoryId = purchaseCategoryId;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    public MoneyDTO getValue() {
        return value;
    }

    public void setValue(MoneyDTO value) {
        this.value = value;
    }

    public String getCalculation() {
        return calculation;
    }

    public void setCalculation(String calculation) {
        this.calculation = calculation;
    }
}
