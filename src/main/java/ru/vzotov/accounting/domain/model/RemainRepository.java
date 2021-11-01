package ru.vzotov.accounting.domain.model;

import ru.vzotov.accounting.domain.model.Remain;
import ru.vzotov.accounting.domain.model.RemainId;
import ru.vzotov.banking.domain.model.AccountNumber;

import java.time.LocalDate;
import java.util.List;

public interface RemainRepository {
    Remain find(RemainId id);

    List<Remain> findByDate(LocalDate fromDate, LocalDate toDate);

    List<Remain> findByAccountNumber(AccountNumber accountNumber);

    void store(Remain remain);

    void delete(Remain remain);
}
