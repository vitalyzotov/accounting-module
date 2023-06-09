package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.CategoryNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealNotFoundException;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;
import ru.vzotov.purchase.domain.model.PurchaseId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface DealsFacade {
    AccountingApi.Deal createDeal(LocalDate date, long amount, String currency, String description, String comment,
                                  Long categoryId, Collection<String> receipts, Collection<String> operations, Collection<String> purchases) throws CategoryNotFoundException;

    AccountingApi.Deal modifyDeal(String dealId, LocalDate date, long amount, String currency, String description, String comment,
                                  Long categoryId, Collection<String> receipts, Collection<String> operations, Collection<String> purchases) throws DealNotFoundException, CategoryNotFoundException;

    AccountingApi.Deal deleteDeal(String dealId, boolean deleteReceipts) throws DealNotFoundException;
    List<AccountingApi.Deal> splitDeal(String dealId) throws DealNotFoundException;

    List<AccountingApi.Deal> listDeals(String query, LocalDate from, LocalDate to, Set<AccountingApi.Deal.Expansion> expand);

    AccountingApi.Deal getDeal(String dealId, Set<AccountingApi.Deal.Expansion> expand) throws DealNotFoundException;

    List<PurchaseDTO> listDealPurchases(String dealId);

    void movePurchase(PurchaseId purchaseId, DealId source, DealId target);

    LocalDate getMinDealDate();
    LocalDate getMaxDealDate();
    LocalDate[] getMinMaxDealDates();

    AccountingApi.Deal mergeDeals(List<String> dealsIds);
}
