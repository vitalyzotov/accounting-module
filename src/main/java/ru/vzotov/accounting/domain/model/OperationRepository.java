package ru.vzotov.accounting.domain.model;

import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.Operation;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.domain.model.Money;
import ru.vzotov.person.domain.model.PersonId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface OperationRepository {

    /**
     * Search operation by ID
     * @param id operation ID
     * @return operation; <code>null</code> if not found
     */
    Operation find(OperationId id);

    /**
     * Search operations by date and amount
     *
     * @param owners operation owners
     * @param fromDate start of date range (inclusive)
     * @param toDate   end of date range (inclusive)
     * @param amount   amount of operation
     * @return list of operations
     */
    List<Operation> findByDateAndAmount(Collection<PersonId> owners, LocalDate fromDate, LocalDate toDate, Money amount);

    /**
     * Search operations by date
     *
     * @param fromDate start of date range (inclusive)
     * @param toDate   end of date range (inclusive)
     * @return list of operations
     */
    List<Operation> findByDate(Collection<PersonId> owners, LocalDate fromDate, LocalDate toDate);

    List<Operation> findByAccountAndDate(AccountNumber accountNumber, LocalDate fromDate, LocalDate toDate);

    List<Operation> findByTypeAndDate(Collection<PersonId> owners, OperationType type, LocalDate fromDate, LocalDate toDate);

    void store(Operation operation);

    void delete(Operation operation);

    List<Operation> findWithoutDeals();
}
