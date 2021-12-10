package ru.vzotov.accounting.interfaces.accounting.rest;

import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.application.ReceiptNotFoundException;
import ru.vzotov.cashreceipt.application.ReceiptRegistrationService;
import ru.vzotov.accounting.interfaces.accounting.facade.CashreceiptsFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.ReceiptDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.QRCodeDTO;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.ReceiptRegistrationRequest;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.ReceiptRegistrationResponse;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.GetReceiptResponse;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.GetReceiptsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class ReceiptsController {

    private final ReceiptRegistrationService receiptRegistrationService;

    private final CashreceiptsFacade cashreceiptsFacade;

    @Autowired
    public ReceiptsController(ReceiptRegistrationService receiptRegistrationService, CashreceiptsFacade cashreceiptsFacade) {
        this.receiptRegistrationService = receiptRegistrationService;
        this.cashreceiptsFacade = cashreceiptsFacade;
    }

    @PostMapping("/accounting/receipts/")
    public ReceiptRegistrationResponse registerReceipt(@RequestBody ReceiptRegistrationRequest request) throws ReceiptNotFoundException, IOException {
        final CheckId qrId = receiptRegistrationService.register(new QRCodeData(request.getQrcode()));
        return new ReceiptRegistrationResponse(qrId.value());
    }

    @GetMapping("/accounting/receipts/")
    public GetReceiptsResponse getReceipts(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        final List<ReceiptDTO> checks = cashreceiptsFacade.listAllChecks(from, to);
        return new GetReceiptsResponse(checks);
    }

    @GetMapping(value = "/accounting/qr/")
    public List<QRCodeDTO> getCodes(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return cashreceiptsFacade.listAllCodes(from, to);
    }

    @GetMapping("/accounting/qr/{receiptId}")
    public QRCodeDTO getCode(@PathVariable String receiptId) {
        return cashreceiptsFacade.getCode(receiptId);
    }

    @GetMapping("/accounting/receipts/{fiscalSign}")
    public GetReceiptResponse getReceipt(@PathVariable String fiscalSign, @RequestParam("t") String date,
                                         @RequestParam("s") String sum,
                                         @RequestParam("fn") String fiscalDriveNumber,
                                         @RequestParam("i") String fiscalDocumentNumber,
                                         @RequestParam("n") String operationType) throws ReceiptNotFoundException {
        final String queryString = getCheckQrString(fiscalSign, date, sum, fiscalDriveNumber, fiscalDocumentNumber, operationType);
        final ReceiptDTO check = cashreceiptsFacade.getCheck(queryString);
        if (check == null) throw new ReceiptNotFoundException();
        return new GetReceiptResponse(check);
    }

    @PutMapping("/accounting/receipts/{fiscalSign}")
    public GetReceiptResponse loadReceiptDetails(@PathVariable String fiscalSign, @RequestParam("t") String date,
                                                 @RequestParam("s") String sum,
                                                 @RequestParam("fn") String fiscalDriveNumber,
                                                 @RequestParam("i") String fiscalDocumentNumber,
                                                 @RequestParam("n") String operationType) throws ReceiptNotFoundException {
        final String queryString = getCheckQrString(fiscalSign, date, sum, fiscalDriveNumber, fiscalDocumentNumber, operationType);
        final ReceiptDTO check = cashreceiptsFacade.loadCheckDetails(new QRCodeData(queryString));
        if (check == null) throw new ReceiptNotFoundException();
        return new GetReceiptResponse(check);
    }

    private String getCheckQrString(String fiscalSign, String date, String sum, String fiscalDriveNumber, String fiscalDocumentNumber, String operationType) {
        final Map<String, String> query = new HashMap<>();
        query.put("t", date);
        query.put("s", sum);
        query.put("fn", fiscalDriveNumber);
        query.put("i", fiscalDocumentNumber);
        query.put("fp", fiscalSign);
        query.put("n", operationType);
        return query.entrySet().stream()
                .map(e -> String.join("=", e.getKey(), e.getValue())).collect(Collectors.joining("&"));
    }

}
