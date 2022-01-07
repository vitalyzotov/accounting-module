package ru.vzotov.accounting.interfaces.purchases.rest.dto;

import java.util.List;

public class PurchaseStoreResponse {

    private List<String> purchaseId;

    public PurchaseStoreResponse() {
    }

    public PurchaseStoreResponse(List<String> purchaseId) {
        this.purchaseId = purchaseId;
    }

    public List<String> getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(List<String> purchaseId) {
        this.purchaseId = purchaseId;
    }
}
