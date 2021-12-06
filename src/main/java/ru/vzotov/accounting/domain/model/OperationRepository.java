package ru.vzotov.accounting.domain.model;

import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.Operation;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.domain.model.Money;

import java.time.LocalDate;
import java.util.List;

public interface OperationRepository {

    Operation find(OperationId id);

    /**
     * Поиск операций по дате и сумме
     *
     * @param fromDate начало периода для поиска
     * @param toDate   окончание периода для поиска
     * @param amount   сумма операции
     * @return список найденных операций
     */
    List<Operation> findByDateAndAmount(LocalDate fromDate, LocalDate toDate, Money amount);

    /**
     * Поиск операций по дате
     *
     * @param fromDate начало периода для поиска
     * @param toDate   окончание периода для поиска
     * @return список найденных операций
     */
    List<Operation> findByDate(LocalDate fromDate, LocalDate toDate);

    List<Operation> findByAccountAndDate(AccountNumber accountNumber, LocalDate fromDate, LocalDate toDate);

    List<Operation> findByTypeAndDate(OperationType type, LocalDate fromDate, LocalDate toDate);

    void store(Operation operation);

    void delete(Operation operation);

    List<Operation> findWithoutDeals();
}
