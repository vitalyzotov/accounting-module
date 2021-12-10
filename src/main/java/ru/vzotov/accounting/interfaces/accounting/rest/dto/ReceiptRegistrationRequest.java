package ru.vzotov.accounting.interfaces.accounting.rest.dto;

@SuppressWarnings("unused")
public class ReceiptRegistrationRequest {

    private String qrcode;

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}
