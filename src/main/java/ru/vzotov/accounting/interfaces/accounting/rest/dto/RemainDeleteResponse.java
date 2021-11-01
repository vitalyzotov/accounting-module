package ru.vzotov.accounting.interfaces.accounting.rest.dto;

public class RemainDeleteResponse {
    private String remainId;

    public RemainDeleteResponse() {
    }

    public RemainDeleteResponse(String remainId) {
        this.remainId = remainId;
    }

    public String getRemainId() {
        return remainId;
    }

    public void setRemainId(String remainId) {
        this.remainId = remainId;
    }
}
