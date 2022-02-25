package ru.vzotov.accounting.interfaces.accounting.facade.dto;

public class PurchaseCategoryDTO {

    private String categoryId;

    private String owner;

    private String name;

    private String parentId;

    public PurchaseCategoryDTO() {
    }

    public PurchaseCategoryDTO(String categoryId, String owner, String name) {
        this(categoryId, owner, name, null);
    }

    public PurchaseCategoryDTO(String categoryId, String owner, String name, String parentId) {
        this.categoryId = categoryId;
        this.owner = owner;
        this.name = name;
        this.parentId = parentId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
