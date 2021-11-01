package ru.vzotov.accounting.domain.model;

import ru.vzotov.banking.domain.model.Account;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.BankId;
import ru.vzotov.banking.domain.model.CardNumber;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

public interface AccountRepository {
    Account find(AccountNumber accountNumber);
    Account findAccountOfCard(CardNumber cardNumber, LocalDate date);
    List<Account> findAll();
    List<Account> find(BankId bankId, Currency currency);
    List<Account> findByAlias(String alias);
    void store(Account account);
}
