package ru.vzotov.accounting.interfaces.purchases.rest;

import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.interfaces.purchases.facade.PurchasesFacade;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;
import ru.vzotov.accounting.interfaces.purchases.rest.dto.PurchaseCreateRequest;
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
import java.util.List;

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

    @PostMapping(params = {"!receiptId"})
    public PurchaseStoreResponse newPurchase(@RequestBody PurchaseCreateRequest purchase) {
        final PurchaseDTO dto = toPurchaseDTO(purchase);

        final PurchaseId pid = purchasesFacade.createPurchase(dto, new DealId(purchase.getDealId()));
        return new PurchaseStoreResponse(pid.value());
    }

    @PostMapping(params = {"receiptId"})
    public List<PurchaseDTO> createPurchasesFromReceipt(@RequestParam String receiptId) {
        return purchasesFacade.createPurchasesFromReceipt(receiptId);
    }

    @PutMapping("{purchaseId}")
    public PurchaseStoreResponse modifyPurchase(@PathVariable String purchaseId, @RequestBody PurchaseModifyRequest purchase) {
        final PurchaseDTO dto = toPurchaseDTO(purchase);
        dto.setPurchaseId(purchaseId);
        purchasesFacade.modifyPurchase(dto);
        return new PurchaseStoreResponse(purchaseId);
    }

    PurchaseDTO toPurchaseDTO(@RequestBody PurchaseDataRequest purchase) {
        final PurchaseDTO dto = new PurchaseDTO();
        dto.setCheckId(purchase.getCheckId());
        dto.setName(purchase.getName());
        dto.setDateTime(purchase.getDateTime());
        dto.setPrice(purchase.getPrice());
        dto.setQuantity(purchase.getQuantity());
        dto.setCategoryId(purchase.getCategoryId());
        return dto;
    }
}