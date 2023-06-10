package ru.vzotov.accounting.interfaces.purchases.facade;

import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.interfaces.accounting.facade.DealNotFoundException;
import ru.vzotov.accounting.interfaces.purchases.PurchasesApi.Purchase;
import ru.vzotov.purchase.domain.model.PurchaseId;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Фасад для работы с покупками
 */
public interface PurchasesFacade {

    Purchase getPurchaseById(String purchaseId);

    PurchaseId deletePurchaseById(String purchaseId);

    List<Purchase> findPurchases(LocalDateTime from, LocalDateTime to);


    void modifyPurchase(Purchase purchase);

    List<PurchaseId> createPurchase(Collection<Purchase> purchases, DealId dealId);

    List<Purchase> createPurchasesFromReceipt(String receiptId);

    @Transactional(value = "accounting-tx")
    List<Purchase> createPurchasesFromDealReceipts(String dealId) throws DealNotFoundException;
}
