package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.QRCode;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.Receipt;
import ru.vzotov.accounting.interfaces.accounting.facade.ReceiptsFacade;
import ru.vzotov.cashreceipt.application.ReceiptNotFoundException;
import ru.vzotov.cashreceipt.application.ReceiptRegistrationService;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class ReceiptsController {

    private final ReceiptRegistrationService receiptRegistrationService;

    private final ReceiptsFacade receiptsFacade;

    @Autowired
    public ReceiptsController(ReceiptRegistrationService receiptRegistrationService, ReceiptsFacade receiptsFacade) {
        this.receiptRegistrationService = receiptRegistrationService;
        this.receiptsFacade = receiptsFacade;
    }

    @PostMapping("/accounting/receipts/")
    public Receipt.Ref registerReceipt(@RequestBody Receipt.Register request)
            throws ReceiptNotFoundException, IOException {
        final ReceiptId qrId = receiptRegistrationService.register(new QRCodeData(request.qrcode()));
        return new Receipt.Ref(qrId.value());
    }

    @GetMapping("/accounting/receipts/")
    public Receipt.Many getReceipts(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return new Receipt.Many(receiptsFacade.listAllReceipts(from, to));
    }

    @GetMapping(value = "/accounting/qr/")
    public List<QRCode> getCodes(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return receiptsFacade.listAllCodes(from, to);
    }

    @GetMapping("/accounting/qr/{receiptId}")
    public ResponseEntity<QRCode> getCode(@PathVariable String receiptId) {
        return ResponseEntity.of(Optional.ofNullable(receiptsFacade.getCode(receiptId)));
    }

    @GetMapping("/accounting/receipts/{fiscalSign}")
    public Receipt.One getReceipt(@PathVariable String fiscalSign,
                                  @RequestParam("t") String date,
                                  @RequestParam("s") String sum,
                                  @RequestParam("fn") String fiscalDriveNumber,
                                  @RequestParam("i") String fiscalDocumentNumber,
                                  @RequestParam("n") String operationType)
            throws ReceiptNotFoundException {
        final String query = getQrString(fiscalSign, date, sum, fiscalDriveNumber, fiscalDocumentNumber, operationType);
        final Receipt receipt = receiptsFacade.getReceipt(query);
        if (receipt == null) throw new ReceiptNotFoundException();
        return new Receipt.One(receipt);
    }

    @PutMapping("/accounting/receipts/{fiscalSign}")
    public Receipt.One loadReceiptDetails(@PathVariable String fiscalSign,
                                          @RequestParam("t") String date,
                                          @RequestParam("s") String sum,
                                          @RequestParam("fn") String fiscalDriveNumber,
                                          @RequestParam("i") String fiscalDocumentNumber,
                                          @RequestParam("n") String operationType)
            throws ReceiptNotFoundException {
        final String query = getQrString(fiscalSign, date, sum, fiscalDriveNumber, fiscalDocumentNumber, operationType);
        final Receipt receipt = receiptsFacade.loadReceiptDetails(new QRCodeData(query));
        if (receipt == null) throw new ReceiptNotFoundException();
        return new Receipt.One(receipt);
    }

    private String getQrString(String fiscalSign,
                               String date,
                               String sum,
                               String fiscalDriveNumber,
                               String fiscalDocumentNumber,
                               String operationType) {
        final Map<String, String> query = new HashMap<>();
        query.put("t", date);
        query.put("s", sum);
        query.put("fn", fiscalDriveNumber);
        query.put("i", fiscalDocumentNumber);
        query.put("fp", fiscalSign);
        query.put("n", operationType);
        return query.entrySet().stream()
                .map(e -> String.join("=", e.getKey(), e.getValue()))
                .collect(Collectors.joining("&"));
    }

}
