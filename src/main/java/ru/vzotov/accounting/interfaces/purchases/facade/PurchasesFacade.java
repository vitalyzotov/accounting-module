package ru.vzotov.accounting.interfaces.purchases.facade;

import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;
import ru.vzotov.purchase.domain.model.PurchaseId;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Фасад для работы с покупками
 */
public interface PurchasesFacade {

    PurchaseDTO getPurchaseById(String purchaseId);
    PurchaseId deletePurchaseById(String purchaseId);
    List<PurchaseDTO> findPurchases(LocalDateTime from, LocalDateTime to);
    PurchaseId storePurchase(PurchaseDTO purchase);
    List<PurchaseDTO> createPurchasesFromCheck(String checkId);
}
