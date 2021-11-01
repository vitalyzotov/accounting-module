package ru.vzotov.accounting.interfaces.accounting.rest.dto;

public class BudgetCategoryModifyRequest {

    private String name;

    private String color;

    private String icon;

    public BudgetCategoryModifyRequest() {
    }

    public BudgetCategoryModifyRequest(String name, String color, String icon) {
        this.name = name;
        this.color = color;
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
