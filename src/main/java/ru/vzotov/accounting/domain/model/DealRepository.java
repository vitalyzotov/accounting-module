package ru.vzotov.accounting.domain.model;

import java.time.LocalDate;
import java.util.List;

public interface DealRepository {
    Deal find(DealId dealId);

    List<Deal> findByDate(LocalDate fromDate, LocalDate toDate);

    void store(Deal deal);

    void delete(Deal deal);
}
