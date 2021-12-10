package ru.vzotov.cashreceipt.application.nalogru2;

public class TicketRequest {
    String qr;

    public TicketRequest() {
    }

    public TicketRequest(String qr) {
        this.qr = qr;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }
}
