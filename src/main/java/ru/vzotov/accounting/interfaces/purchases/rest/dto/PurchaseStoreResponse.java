package ru.vzotov.accounting.interfaces.purchases.rest.dto;

public class PurchaseStoreResponse {

    private String purchaseId;

    public PurchaseStoreResponse() {
    }

    public PurchaseStoreResponse(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }
}
