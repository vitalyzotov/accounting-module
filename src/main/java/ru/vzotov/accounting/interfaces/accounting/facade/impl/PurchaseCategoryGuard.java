package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import ru.vzotov.cashreceipt.domain.model.PurchaseCategory;

public interface PurchaseCategoryGuard {
    PurchaseCategory accessing(PurchaseCategory entity);
}
