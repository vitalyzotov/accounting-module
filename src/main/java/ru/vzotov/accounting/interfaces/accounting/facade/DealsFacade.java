package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.Deal;
import ru.vzotov.accounting.interfaces.purchases.PurchasesApi.Purchase;
import ru.vzotov.purchase.domain.model.PurchaseId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface DealsFacade {
    Deal createDeal(LocalDate date, long amount, String currency, String description, String comment, Long categoryId,
                    Collection<String> receipts, Collection<String> operations, Collection<String> purchases)
            throws CategoryNotFoundException;

    Deal modifyDeal(String dealId,
                    LocalDate date, long amount, String currency, String description, String comment, Long categoryId,
                    Collection<String> receipts, Collection<String> operations, Collection<String> purchases)
            throws DealNotFoundException, CategoryNotFoundException;

    Deal deleteDeal(String dealId, boolean deleteReceipts) throws DealNotFoundException;

    List<Deal> splitDeal(String dealId) throws DealNotFoundException;

    List<Deal> listDeals(String query, LocalDate from, LocalDate to, Set<Deal.Expansion> expand);

    Deal getDeal(String dealId, Set<Deal.Expansion> expand) throws DealNotFoundException;

    List<Purchase> listDealPurchases(String dealId);

    void movePurchase(PurchaseId purchaseId, DealId source, DealId target);

    LocalDate getMinDealDate();

    LocalDate getMaxDealDate();

    LocalDate[] getMinMaxDealDates();

    Deal mergeDeals(List<String> dealsIds);
}
