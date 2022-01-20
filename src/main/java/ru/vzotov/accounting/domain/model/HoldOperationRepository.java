package ru.vzotov.accounting.domain.model;

import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.HoldId;
import ru.vzotov.banking.domain.model.HoldOperation;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.domain.model.Money;
import ru.vzotov.person.domain.model.PersonId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface HoldOperationRepository {

    HoldOperation find(HoldId id);

    HoldOperation find(AccountNumber account, LocalDate date, Money amount, OperationType type, String description);

    List<HoldOperation> findByAccountAndDate(AccountNumber accountNumber, LocalDate fromDate, LocalDate toDate);

    List<HoldOperation> findByDate(Collection<PersonId> owners, LocalDate fromDate, LocalDate toDate);

    List<HoldOperation> findByTypeAndDate(Collection<PersonId> owners, OperationType type, LocalDate fromDate, LocalDate toDate);

    void store(HoldOperation operation);

    void delete(HoldOperation operation);

}
