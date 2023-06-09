package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.Deal;
import ru.vzotov.accounting.interfaces.accounting.facade.DealsFacade;
import ru.vzotov.accounting.interfaces.purchases.PurchasesApi.Purchase;
import ru.vzotov.purchase.domain.model.PurchaseId;

import java.util.List;

@RestController
@RequestMapping("/accounting/deals/{dealId}/purchases")
@CrossOrigin
public class DealPurchasesController {

    private final DealsFacade dealsFacade;

    public DealPurchasesController(DealsFacade dealsFacade) {
        this.dealsFacade = dealsFacade;
    }

    @GetMapping
    public List<Purchase> listDealPurchases(@PathVariable String dealId) {
        return dealsFacade.listDealPurchases(dealId);
    }

    @PutMapping("{purchaseId}")
    public void moveDealPurchase(@PathVariable String dealId, @PathVariable String purchaseId,
                                 @RequestBody Deal.Ref target) {
        dealsFacade.movePurchase(new PurchaseId(purchaseId), new DealId(dealId), new DealId(target.dealId()));
    }
}
