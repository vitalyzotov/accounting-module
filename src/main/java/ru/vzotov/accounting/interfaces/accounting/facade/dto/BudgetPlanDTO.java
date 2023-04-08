package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public class BudgetPlanDTO {
    private String itemId;

    private String direction;

    private String sourceAccount;

    private String targetAccount;

    private Long categoryId;

    private String purchaseCategoryId;

    private MoneyDTO value;

    private String ruleId;

    private LocalDate date;

    public BudgetPlanDTO() {
    }

    public BudgetPlanDTO(String itemId, String direction, String sourceAccount, String targetAccount, Long categoryId, String purchaseCategoryId, MoneyDTO value, String ruleId, LocalDate date) {
        this.itemId = itemId;
        this.direction = direction;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.categoryId = categoryId;
        this.purchaseCategoryId = purchaseCategoryId;
        this.value = value;
        this.ruleId = ruleId;
        this.date = date;
    }

    public String getPurchaseCategoryId() {
        return purchaseCategoryId;
    }

    public void setPurchaseCategoryId(String purchaseCategoryId) {
        this.purchaseCategoryId = purchaseCategoryId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
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

    public MoneyDTO getValue() {
        return value;
    }

    public void setValue(MoneyDTO value) {
        this.value = value;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
