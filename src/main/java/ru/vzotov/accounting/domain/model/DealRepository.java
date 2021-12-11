package ru.vzotov.accounting.domain.model;

import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.cashreceipt.domain.model.CheckId;

import java.time.LocalDate;
import java.util.List;

public interface DealRepository {
    Deal find(DealId dealId);

    List<Deal> findByDate(LocalDate fromDate, LocalDate toDate);

    Deal findByOperation(OperationId operation);

    Deal findByReceipt(CheckId receiptId);

    void store(Deal deal);

    void delete(Deal deal);

    LocalDate findMinDealDate();

    LocalDate findMaxDealDate();
}
