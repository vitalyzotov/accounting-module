package ru.vzotov.accounting.interfaces.accounting.rest.dto;

public class ReceiptItemCategoryPatch {
    private String category;

    public ReceiptItemCategoryPatch() {
    }

    public ReceiptItemCategoryPatch(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
