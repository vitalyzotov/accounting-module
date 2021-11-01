package ru.vzotov.accounting.domain.model;

import ru.vzotov.banking.domain.model.Bank;
import ru.vzotov.banking.domain.model.BankId;

import java.util.List;

public interface BankRepository {

    Bank find(BankId bankId);

    List<Bank> findAll();

    void store(Bank bank);

    void delete(Bank bank);

}
