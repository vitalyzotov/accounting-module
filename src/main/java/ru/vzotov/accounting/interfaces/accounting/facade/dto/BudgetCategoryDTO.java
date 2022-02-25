package ru.vzotov.accounting.interfaces.accounting.facade.dto;

public class BudgetCategoryDTO {
    private long id;
    private String owner;
    private String name;
    private String color;
    private String icon;

    public BudgetCategoryDTO() {
    }

    public BudgetCategoryDTO(long id, String owner, String name) {
        this(id, owner, name, null, null);
    }

    public BudgetCategoryDTO(long id, String owner, String name, String color) {
        this(id, owner, name, color, null);
    }

    public BudgetCategoryDTO(long id, String owner, String name, String color, String icon) {
        this.id = id;
        this.owner = owner;
        this.name = name;
        this.color = color;
        this.icon = icon;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
