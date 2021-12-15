package ru.vzotov.accounting.interfaces.purchases.facade.dto;

public class PurchaseRef {
    private String purchaseId;

    public PurchaseRef() {
    }

    public PurchaseRef(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

}
