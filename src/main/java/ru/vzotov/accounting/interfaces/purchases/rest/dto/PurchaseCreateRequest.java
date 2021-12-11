package ru.vzotov.accounting.interfaces.purchases.rest.dto;

public class PurchaseCreateRequest extends PurchaseDataRequest {

    public PurchaseCreateRequest() {
    }

    public PurchaseCreateRequest(String dealId) {
        this.dealId = dealId;
    }

    private String dealId;

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }
}
