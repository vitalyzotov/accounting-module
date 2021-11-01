package ru.vzotov.accounting.interfaces.accounting.rest.dto;

public class RemainCreateResponse {

    private String remainId;

    public RemainCreateResponse() {

    }

    public RemainCreateResponse(String remainId) {
        this.remainId = remainId;
    }

    public String getRemainId() {
        return remainId;
    }

    public void setRemainId(String remainId) {
        this.remainId = remainId;
    }
}
