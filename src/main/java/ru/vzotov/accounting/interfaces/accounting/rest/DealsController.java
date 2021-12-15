package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.beans.factory.annotation.Autowired;
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
import ru.vzotov.accounting.interfaces.accounting.facade.DealsFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.CategoryNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.OperationRef;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.ReceiptRef;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.DealsMetadataResponse;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.MergeDealsRequest;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseRef;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/accounting/deals")
@CrossOrigin
public class DealsController {

    @Autowired
    private DealsFacade dealsFacade;

    @GetMapping
    public List<DealDTO> listDeals(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                                   @RequestParam(required = false) List<String> expand) {
        return dealsFacade.listDeals(from, to, expand == null ? emptySet() : new HashSet<>(expand));
    }

    @GetMapping("metadata")
    public DealsMetadataResponse getDealsMeta() {
        return new DealsMetadataResponse(
                dealsFacade.getMinDealDate(),
                dealsFacade.getMaxDealDate()
        );
    }

    @GetMapping("{dealId}")
    public DealDTO getDeal(@PathVariable String dealId) throws DealNotFoundException {
        return dealsFacade.getDeal(dealId);
    }

    @DeleteMapping("{dealId}")
    public DealDTO deleteDeal(@PathVariable String dealId) throws DealNotFoundException {
        return dealsFacade.deleteDeal(dealId);
    }

    @PostMapping
    public DealDTO createDeal(@RequestBody DealDTO deal) throws CategoryNotFoundException {
        return dealsFacade.createDeal(deal.getDate(), deal.getAmount().getAmount(), deal.getAmount().getCurrency(),
                deal.getDescription(), deal.getComment(), deal.getCategory(),
                ofNullable(deal.getReceipts()).orElse(emptyList()).stream()
                        .map(ReceiptRef::getCheckId).collect(toSet()),
                ofNullable(deal.getOperations()).orElse(emptyList()).stream()
                        .map(OperationRef::getOperationId).collect(toSet()),
                ofNullable(deal.getPurchases()).orElse(emptyList()).stream()
                        .map(PurchaseRef::getPurchaseId).collect(Collectors.toList()));
    }

    @PutMapping("{dealId}")
    public void modifyDeal(@PathVariable String dealId, @RequestBody DealDTO deal
    ) throws DealNotFoundException, CategoryNotFoundException {
        deal.setDealId(dealId);
        dealsFacade.modifyDeal(deal.getDealId(), deal.getDate(),
                deal.getAmount().getAmount(), deal.getAmount().getCurrency(),
                deal.getDescription(), deal.getComment(), deal.getCategory(),
                ofNullable(deal.getReceipts()).orElse(emptyList()).stream()
                        .map(ReceiptRef::getCheckId).collect(toSet()),
                ofNullable(deal.getOperations()).orElse(emptyList()).stream()
                        .map(OperationRef::getOperationId).collect(toSet()),
                ofNullable(deal.getPurchases()).orElse(emptyList()).stream()
                        .map(PurchaseRef::getPurchaseId).collect(Collectors.toList()));
    }

    @PatchMapping
    public DealDTO mergeDeals(@RequestBody MergeDealsRequest body) {
        return dealsFacade.mergeDeals(body.getDeals());
    }
}
