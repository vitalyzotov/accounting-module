package ru.vzotov.accounting.interfaces.accounting.rest.dto;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.ReceiptDTO;

public class GetReceiptResponse {
    private ReceiptDTO receipt;

    public GetReceiptResponse() {
    }

    public GetReceiptResponse(ReceiptDTO receipt) {
        this.receipt = receipt;
    }

    public ReceiptDTO getReceipt() {
        return receipt;
    }

    public void setReceipt(ReceiptDTO receipt) {
        this.receipt = receipt;
    }
}
