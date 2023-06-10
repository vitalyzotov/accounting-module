package ru.vzotov.accounting.interfaces.purchases.rest;

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
import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.interfaces.accounting.facade.DealNotFoundException;
import ru.vzotov.accounting.interfaces.purchases.PurchasesApi.Purchase;
import ru.vzotov.accounting.interfaces.purchases.facade.PurchasesFacade;
import ru.vzotov.purchase.domain.model.PurchaseId;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/accounting/purchases")
@CrossOrigin
public class PurchasesController {

    private final PurchasesFacade purchasesFacade;

    @Autowired
    public PurchasesController(PurchasesFacade purchasesFacade) {
        this.purchasesFacade = purchasesFacade;
    }

    @GetMapping("{purchaseId}")
    public Purchase getPurchaseById(@PathVariable String purchaseId) {
        return purchasesFacade.getPurchaseById(purchaseId);
    }

    @DeleteMapping("{purchaseId}")
    public Purchase.Ref deletePurchaseById(@PathVariable String purchaseId) {
        PurchaseId id = purchasesFacade.deletePurchaseById(purchaseId);
        return new Purchase.Ref(id.value());
    }

    @GetMapping
    public List<Purchase> searchPurchases(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return purchasesFacade.findPurchases(from, to);
    }

    @PostMapping(params = {"!receiptId", "!dealId"})
    public Purchase.Refs newPurchase(@RequestBody Purchase.Create purchase) {
        final List<Purchase> dtoList = purchase.purchases().stream()
                .map(data -> toPurchase(null, data))
                .toList();

        final List<PurchaseId> pid = purchasesFacade.createPurchase(dtoList, new DealId(purchase.dealId()));
        return new Purchase.Refs(pid.stream().map(PurchaseId::value).toList());
    }

    @PostMapping(params = {"receiptId"})
    public List<Purchase> createPurchasesFromReceipt(@RequestParam String receiptId) {
        return purchasesFacade.createPurchasesFromReceipt(receiptId);
    }

    @PostMapping(params = {"dealId"})
    public List<Purchase> createPurchasesFromDealReceipts(@RequestParam String dealId) throws DealNotFoundException {
        return purchasesFacade.createPurchasesFromDealReceipts(dealId);
    }

    @PutMapping("{purchaseId}")
    public Purchase.Refs modifyPurchase(@PathVariable String purchaseId, @RequestBody Purchase.Data purchase) {
        final Purchase dto = toPurchase(purchaseId, purchase);
        purchasesFacade.modifyPurchase(dto);
        return new Purchase.Refs(Collections.singletonList(purchaseId));
    }

    Purchase toPurchase(String purchaseId, Purchase.Data purchase) {
        return new Purchase(
                purchaseId,
                null,
                purchase.receiptId(),
                purchase.name(),
                purchase.dateTime(),
                purchase.price(),
                purchase.quantity(),
                purchase.categoryId()
        );
    }

}
