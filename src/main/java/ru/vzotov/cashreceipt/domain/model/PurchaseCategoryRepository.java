package ru.vzotov.cashreceipt.domain.model;

import ru.vzotov.person.domain.model.PersonId;

import java.util.List;

public interface PurchaseCategoryRepository {

    PurchaseCategory findById(PurchaseCategoryId id);

    PurchaseCategory findByName(PersonId owner, String name);

    List<PurchaseCategory> findAll(PersonId owner);

    void store(PurchaseCategory category);

}
