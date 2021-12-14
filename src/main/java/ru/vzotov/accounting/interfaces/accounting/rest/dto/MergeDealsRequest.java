package ru.vzotov.accounting.interfaces.accounting.rest.dto;

import java.util.List;

public class MergeDealsRequest {

    private List<String> deals;

    public MergeDealsRequest() {
    }

    public MergeDealsRequest(List<String> deals) {
        this.deals = deals;
    }

    public List<String> getDeals() {
        return deals;
    }

    public void setDeals(List<String> deals) {
        this.deals = deals;
    }
}
