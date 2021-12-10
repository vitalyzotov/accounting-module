package ru.vzotov.accounting.interfaces.accounting.rest.dto;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.ReceiptDTO;

import java.util.List;

public class GetReceiptsResponse {
    private List<ReceiptDTO> checks;

    private List<ReceiptDTO> qrCodes;

    public GetReceiptsResponse() {
    }

    public GetReceiptsResponse(List<ReceiptDTO> checks) {
        this.checks = checks;
    }

    public List<ReceiptDTO> getChecks() {
        return checks;
    }

}
