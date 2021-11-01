package ru.vzotov.accounting.domain.model;

import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository {
    Transaction find(OperationId id);
    List<Transaction> findByDate(LocalDate fromDate, LocalDate toDate);
    void store(Transaction transaction);
    void delete(Transaction transaction);
}
