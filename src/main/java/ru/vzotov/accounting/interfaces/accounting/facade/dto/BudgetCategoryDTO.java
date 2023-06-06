package ru.vzotov.accounting.interfaces.accounting.facade.dto;

public record BudgetCategoryDTO(long id, String owner, String name, String color, String icon) {
    public BudgetCategoryDTO(long id, String owner, String name) {
        this(id, owner, name, null, null);
    }

    public BudgetCategoryDTO(long id, String owner, String name, String color) {
        this(id, owner, name, color, null);
    }

}
