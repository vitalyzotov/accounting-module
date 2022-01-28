package ru.vzotov.accounting.domain.model;

import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.person.domain.model.PersonId;
import ru.vzotov.purchase.domain.model.PurchaseId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface DealRepository {
    Deal find(DealId dealId);

    List<Deal> findByDate(LocalDate fromDate, LocalDate toDate);

    List<Deal> findByDate(Collection<PersonId> owners, LocalDate fromDate, LocalDate toDate);

    Deal findByOperation(OperationId operation);

    Deal findByReceipt(ReceiptId receiptId);

    Deal findByPurchase(PurchaseId purchaseId);

    void store(Deal deal);

    void delete(Deal deal);

    LocalDate findMinDealDate(PersonId owner);

    LocalDate findMaxDealDate(PersonId owner);
}
