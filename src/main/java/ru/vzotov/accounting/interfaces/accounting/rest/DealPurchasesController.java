package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.accounting.interfaces.accounting.facade.DealsSharedFacade;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;

import java.util.List;

@RestController
@RequestMapping("/accounting/deals/{dealId}/purchases")
@CrossOrigin
public class DealPurchasesController {

    private final DealsSharedFacade dealsSharedFacade;

    public DealPurchasesController(DealsSharedFacade dealsSharedFacade) {
        this.dealsSharedFacade = dealsSharedFacade;
    }

    @GetMapping
    public List<PurchaseDTO> listDealPurchases(@PathVariable String dealId) {
        return dealsSharedFacade.listDealPurchases(dealId);
    }
}
