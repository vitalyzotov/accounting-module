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
import ru.vzotov.accounting.interfaces.accounting.rest.dto.DealsMetadataResponse;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.MergeDealsRequest;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/accounting/deals")
@CrossOrigin
public class DealsController {

    @Autowired
    private DealsFacade dealsFacade;

    @GetMapping
    public List<DealDTO> listDeals(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return dealsFacade.listDeals(from, to);
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
                deal.getDescription(), deal.getComment(), deal.getCategory(), deal.getReceipts(), deal.getOperations(), deal.getPurchases());
    }

    @PutMapping("{dealId}")
    public void modifyDeal(@PathVariable String dealId, @RequestBody DealDTO deal
    ) throws DealNotFoundException, CategoryNotFoundException {
        deal.setDealId(dealId);
        dealsFacade.modifyDeal(deal.getDealId(), deal.getDate(),
                deal.getAmount().getAmount(), deal.getAmount().getCurrency(),
                deal.getDescription(), deal.getComment(), deal.getCategory(), deal.getReceipts(), deal.getOperations(), deal.getPurchases());
    }

    @PatchMapping
    public DealDTO mergeDeals(@RequestBody MergeDealsRequest body) {
        return dealsFacade.mergeDeals(body.getDeals());
    }
}
