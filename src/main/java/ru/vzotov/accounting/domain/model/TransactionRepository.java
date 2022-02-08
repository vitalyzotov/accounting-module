package ru.vzotov.accounting.domain.model;

import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.Transaction;
import ru.vzotov.person.domain.model.PersonId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface TransactionRepository {
    Transaction find(OperationId id);
    List<Transaction> findByDate(Collection<PersonId> owners, LocalDate fromDate, LocalDate toDate);
    void store(Transaction transaction);
    void delete(Transaction transaction);
}
