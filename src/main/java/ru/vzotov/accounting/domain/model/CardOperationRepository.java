package ru.vzotov.accounting.domain.model;

import ru.vzotov.banking.domain.model.CardOperation;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.domain.model.Money;

import java.time.LocalDate;
import java.util.List;

public interface CardOperationRepository {

    CardOperation find(OperationId id);

    /**
     * Поиск операций по дате и сумме
     *
     * @param fromDate начало периода для поиска
     * @param toDate   окончание периода для поиска
     * @param amount   сумма операции
     * @return список найденных операций
     */
    List<CardOperation> findByDateAndAmount(LocalDate fromDate, LocalDate toDate, Money amount);

    /**
     * Поиск операций по дате
     *
     * @param fromDate начало периода для поиска
     * @param toDate   окончание периода для поиска
     * @return список найденных операций
     */
    List<CardOperation> findByDate(LocalDate fromDate, LocalDate toDate);

    void store(CardOperation operation);


}
