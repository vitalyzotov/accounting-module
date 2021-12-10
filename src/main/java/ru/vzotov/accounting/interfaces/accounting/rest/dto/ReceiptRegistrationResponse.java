package ru.vzotov.accounting.interfaces.accounting.rest.dto;

@SuppressWarnings("unused")
public class ReceiptRegistrationResponse {

    private String id;

    public ReceiptRegistrationResponse() {
    }

    public ReceiptRegistrationResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
