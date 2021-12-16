package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.CategoryNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealNotFoundException;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;
import ru.vzotov.purchase.domain.model.PurchaseId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface DealsFacade {
    DealDTO createDeal(LocalDate date, long amount, String currency, String description, String comment,
                       Long categoryId, Collection<String> receipts, Collection<String> operations, Collection<String> purchases) throws CategoryNotFoundException;

    DealDTO modifyDeal(String dealId, LocalDate date, long amount, String currency, String description, String comment,
                       Long categoryId, Collection<String> receipts, Collection<String> operations, Collection<String> purchases) throws DealNotFoundException, CategoryNotFoundException;

    DealDTO deleteDeal(String dealId) throws DealNotFoundException;
    List<DealDTO> splitDeal(String dealId) throws DealNotFoundException;

    List<DealDTO> listDeals(LocalDate from, LocalDate to, Set<String> expand);

    DealDTO getDeal(String dealId) throws DealNotFoundException;

    List<PurchaseDTO> listDealPurchases(String dealId);

    void movePurchase(PurchaseId purchaseId, DealId source, DealId target);

    LocalDate getMinDealDate();
    LocalDate getMaxDealDate();

    DealDTO mergeDeals(List<String> dealsIds);
}
