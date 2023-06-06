package ru.vzotov.accounting.interfaces.accounting.facade.dto;

public record PurchaseCategoryDTO(String categoryId, String owner, String name, String parentId) {

    public PurchaseCategoryDTO(String categoryId, String owner, String name) {
        this(categoryId, owner, name, null);
    }

}
