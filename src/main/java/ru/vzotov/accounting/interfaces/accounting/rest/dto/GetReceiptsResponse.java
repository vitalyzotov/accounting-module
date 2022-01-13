package ru.vzotov.accounting.interfaces.accounting.rest.dto;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.ReceiptDTO;

import java.util.List;

public class GetReceiptsResponse {
    private List<ReceiptDTO> receipts;

    private List<ReceiptDTO> qrCodes;

    public GetReceiptsResponse() {
    }

    public GetReceiptsResponse(List<ReceiptDTO> receipts) {
        this.receipts = receipts;
    }

    public List<ReceiptDTO> getReceipts() {
        return receipts;
    }

}
