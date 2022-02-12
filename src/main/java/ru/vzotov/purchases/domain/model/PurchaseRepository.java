package ru.vzotov.purchases.domain.model;

import ru.vzotov.person.domain.model.PersonId;
import ru.vzotov.purchase.domain.model.Purchase;
import ru.vzotov.purchase.domain.model.PurchaseId;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface PurchaseRepository {
    Purchase find(PurchaseId id);

    void store(Purchase purchase);

    List<Purchase> findByDate(Collection<PersonId> owners, LocalDateTime fromDateTime, LocalDateTime toDateTime);

    boolean delete(PurchaseId id);
}
