package ru.vzotov.accounting.interfaces.purchases.rest.dto;

public class PurchaseDeleteResponse {

    private String purchaseId;

    public PurchaseDeleteResponse() {
    }

    public PurchaseDeleteResponse(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }
}
