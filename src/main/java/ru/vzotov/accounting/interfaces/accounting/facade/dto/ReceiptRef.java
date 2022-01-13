package ru.vzotov.accounting.interfaces.accounting.facade.dto;

public class ReceiptRef {
    private String receiptId;

    public ReceiptRef() {
    }

    public ReceiptRef(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }
}
