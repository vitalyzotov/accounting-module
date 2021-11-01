package ru.vzotov.accounting.interfaces.accounting.facade.dto;

public class BudgetCategoryDTO {
    private long id;
    private String name;
    private String color;
    private String icon;

    public BudgetCategoryDTO() {
    }

    public BudgetCategoryDTO(long id, String name) {
        this(id, name, null, null);
    }

    public BudgetCategoryDTO(long id, String name, String color) {
        this(id, name, color, null);
    }

    public BudgetCategoryDTO(long id, String name, String color, String icon) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.icon = icon;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
