package ru.vzotov.cashreceipt.domain.model;

import java.util.List;

public interface PurchaseCategoryRepository {

    PurchaseCategory findById(PurchaseCategoryId id);

    PurchaseCategory findByName(String name);

    List<PurchaseCategory> findAll();

    void store(PurchaseCategory category);

}
