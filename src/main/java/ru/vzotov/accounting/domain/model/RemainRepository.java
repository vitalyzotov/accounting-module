package ru.vzotov.accounting.domain.model;

import ru.vzotov.accounting.domain.model.Remain;
import ru.vzotov.accounting.domain.model.RemainId;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.person.domain.model.PersonId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface RemainRepository {
    Remain find(RemainId id);

    List<Remain> findByDate(Collection<PersonId> owners, LocalDate fromDate, LocalDate toDate);

    List<Remain> findByAccountNumber(AccountNumber accountNumber);

    void store(Remain remain);

    void delete(Remain remain);
}
