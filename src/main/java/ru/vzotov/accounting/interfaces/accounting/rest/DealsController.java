package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.Deal;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.OperationRef;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.ReceiptRef;
import ru.vzotov.accounting.interfaces.accounting.facade.CategoryNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.DealNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.DealsFacade;
import ru.vzotov.accounting.interfaces.purchases.PurchasesApi.PurchaseRef;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/accounting/deals")
@CrossOrigin
public class DealsController {

    private final DealsFacade dealsFacade;

    public DealsController(DealsFacade dealsFacade) {
        this.dealsFacade = dealsFacade;
    }

    @GetMapping
    public List<Deal> listDeals(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                @RequestParam(required = false) String query,
                                @RequestParam(required = false) List<String> expand) {
        return dealsFacade.listDeals(query, from, to,
                expand == null ? emptySet() : expand.stream().map(Deal.Expansion::of).collect(toSet()));
    }

    @GetMapping("metadata")
    public Deal.Meta getDealsMeta() {
        final LocalDate[] dates = dealsFacade.getMinMaxDealDates();
        return new Deal.Meta(dates[0], dates[1]);
    }

    @GetMapping("{dealId}")
    public Deal getDeal(@PathVariable String dealId, @RequestParam(required = false) List<String> expand)
            throws DealNotFoundException {
        return dealsFacade.getDeal(dealId, expand == null ? emptySet() : expand.stream().map(Deal.Expansion::of).collect(toSet()));
    }

    @DeleteMapping("{dealId}")
    public List<Deal> deleteDeal(@PathVariable String dealId,
                                 @RequestParam(required = false, defaultValue = "false") boolean split,
                                 @RequestParam(required = false, defaultValue = "false") boolean receipt)
            throws DealNotFoundException {
        if (split) {
            return dealsFacade.splitDeal(dealId);
        } else {
            dealsFacade.deleteDeal(dealId, receipt);
            return Collections.emptyList();
        }
    }

    @PostMapping
    public Deal createDeal(@RequestBody Deal deal) throws CategoryNotFoundException {
        return dealsFacade.createDeal(
                deal.date(),
                deal.amount().amount(),
                deal.amount().currency(),
                deal.description(),
                deal.comment(),
                deal.category(),
                ofNullable(deal.receipts()).orElse(emptyList()).stream().map(ReceiptRef::receiptId).collect(toSet()),
                ofNullable(deal.operations()).orElse(emptyList()).stream().map(OperationRef::operationId).collect(toSet()),
                ofNullable(deal.purchases()).orElse(emptyList()).stream().map(PurchaseRef::purchaseId).toList());
    }

    @PutMapping("{dealId}")
    public void modifyDeal(@PathVariable String dealId, @RequestBody Deal deal)
            throws DealNotFoundException, CategoryNotFoundException {
        dealsFacade.modifyDeal(
                dealId,
                deal.date(),
                deal.amount().amount(),
                deal.amount().currency(),
                deal.description(),
                deal.comment(),
                deal.category(),
                ofNullable(deal.receipts()).orElse(emptyList()).stream().map(ReceiptRef::receiptId).collect(toSet()),
                ofNullable(deal.operations()).orElse(emptyList()).stream().map(OperationRef::operationId).collect(toSet()),
                ofNullable(deal.purchases()).orElse(emptyList()).stream().map(PurchaseRef::purchaseId).toList());
    }

    @PatchMapping
    public Deal patchDeals(@RequestBody Deal.Patch body) {
        return dealsFacade.mergeDeals(body.deals());
    }

}
