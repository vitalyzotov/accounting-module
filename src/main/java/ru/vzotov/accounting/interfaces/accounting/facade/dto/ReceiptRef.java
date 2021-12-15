package ru.vzotov.accounting.interfaces.accounting.facade.dto;

public class ReceiptRef {
    private String checkId;

    public ReceiptRef() {
    }

    public ReceiptRef(String checkId) {
        this.checkId = checkId;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }
}
