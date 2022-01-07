package ru.vzotov.accounting.interfaces.purchases.rest.dto;

import java.util.List;

public class PurchaseCreateRequest {

    private String dealId;

    private List<PurchaseData> purchases;

    public PurchaseCreateRequest() {
    }

    public PurchaseCreateRequest(String dealId) {
        this.dealId = dealId;
    }

    public PurchaseCreateRequest(String dealId, List<PurchaseData> purchases) {
        this.dealId = dealId;
        this.purchases = purchases;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public List<PurchaseData> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseData> purchases) {
        this.purchases = purchases;
    }
}
