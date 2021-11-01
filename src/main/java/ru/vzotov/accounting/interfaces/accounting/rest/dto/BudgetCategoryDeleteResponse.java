package ru.vzotov.accounting.interfaces.accounting.rest.dto;

public class BudgetCategoryDeleteResponse {
    private long id;

    public BudgetCategoryDeleteResponse() {
    }

    public BudgetCategoryDeleteResponse(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
