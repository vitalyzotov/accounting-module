package ru.vzotov.accounting.interfaces.accounting.rest.dto;

import java.util.List;

public class PatchDealsRequest {

    private List<String> deals;

    public PatchDealsRequest() {
    }

    public PatchDealsRequest(List<String> deals) {
        this.deals = deals;
    }

    public List<String> getDeals() {
        return deals;
    }

    public void setDeals(List<String> deals) {
        this.deals = deals;
    }
}
