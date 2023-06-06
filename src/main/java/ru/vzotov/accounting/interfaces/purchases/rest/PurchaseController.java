package ru.vzotov.accounting.interfaces.purchases.rest;

import org.apache.commons.lang3.Validate;
import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealNotFoundException;
import ru.vzotov.accounting.interfaces.purchases.facade.PurchasesFacade;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;
import ru.vzotov.accounting.interfaces.purchases.rest.dto.PurchaseCreateRequest;
import ru.vzotov.accounting.interfaces.purchases.rest.dto.PurchaseData;
import ru.vzotov.accounting.interfaces.purchases.rest.dto.PurchaseDataRequest;
import ru.vzotov.accounting.interfaces.purchases.rest.dto.PurchaseDeleteResponse;
import ru.vzotov.accounting.interfaces.purchases.rest.dto.PurchaseModifyRequest;
import ru.vzotov.accounting.interfaces.purchases.rest.dto.PurchaseStoreResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.purchase.domain.model.PurchaseId;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounting/purchases")
@CrossOrigin
public class PurchaseController {

    private final PurchasesFacade purchasesFacade;

    @Autowired
    public PurchaseController(PurchasesFacade purchasesFacade) {
        this.purchasesFacade = purchasesFacade;
    }

    @GetMapping("{purchaseId}")
    public PurchaseDTO getPurchaseById(@PathVariable String purchaseId) {
        return purchasesFacade.getPurchaseById(purchaseId);
    }

    @DeleteMapping("{purchaseId}")
    public PurchaseDeleteResponse deletePurchaseById(@PathVariable String purchaseId) {
        PurchaseId id = purchasesFacade.deletePurchaseById(purchaseId);
        return new PurchaseDeleteResponse(id.value());
    }

    @GetMapping
    public List<PurchaseDTO> searchPurchases(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return purchasesFacade.findPurchases(from, to);
    }

    @PostMapping(params = {"!receiptId", "!dealId"})
    public PurchaseStoreResponse newPurchase(@RequestBody PurchaseCreateRequest purchase) {
        final List<PurchaseDTO> dtoList = purchase.getPurchases().stream()
                .map(data -> toPurchaseDTO(null, data))
                .collect(Collectors.toList());

        final List<PurchaseId> pid = purchasesFacade.createPurchase(dtoList, new DealId(purchase.getDealId()));
        return new PurchaseStoreResponse(pid.stream().map(PurchaseId::value).collect(Collectors.toList()));
    }

    @PostMapping(params = {"receiptId"})
    public List<PurchaseDTO> createPurchasesFromReceipt(@RequestParam String receiptId) {
        return purchasesFacade.createPurchasesFromReceipt(receiptId);
    }

    @PostMapping(params = {"dealId"})
    public List<PurchaseDTO> createPurchasesFromDealReceipts(@RequestParam String dealId) throws DealNotFoundException {
        return purchasesFacade.createPurchasesFromDealReceipts(dealId);
    }

    @PutMapping("{purchaseId}")
    public PurchaseStoreResponse modifyPurchase(@PathVariable String purchaseId, @RequestBody PurchaseModifyRequest purchase) {
        final PurchaseDTO dto = toPurchaseDTO(purchaseId, purchase);
        purchasesFacade.modifyPurchase(dto);
        return new PurchaseStoreResponse(Collections.singletonList(purchaseId));
    }

    PurchaseDTO toPurchaseDTO(String purchaseId, PurchaseDataRequest purchase) {
        return new PurchaseDTO(
                purchaseId,
                null,
                purchase.getReceiptId(),
                purchase.getName(),
                purchase.getDateTime(),
                purchase.getPrice(),
                purchase.getQuantity(),
                purchase.getCategoryId()
        );
    }
}
