package ru.vzotov.accounting.interfaces.accounting.rest.dto;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.ReceiptDTO;

public class GetReceiptResponse {
    private ReceiptDTO check;

    public GetReceiptResponse() {
    }

    public GetReceiptResponse(ReceiptDTO check) {
        this.check = check;
    }

    public ReceiptDTO getCheck() {
        return check;
    }

    public void setCheck(ReceiptDTO check) {
        this.check = check;
    }
}
