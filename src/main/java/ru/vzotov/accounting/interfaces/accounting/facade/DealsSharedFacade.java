package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;

import java.util.List;

public interface DealsSharedFacade {
    List<PurchaseDTO> listDealPurchases(String dealId);
}
