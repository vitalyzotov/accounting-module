package ru.vzotov.accounting.domain.model;

import ru.vzotov.banking.domain.model.Account;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.BankId;
import ru.vzotov.banking.domain.model.CardNumber;
import ru.vzotov.person.domain.model.PersonId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Currency;
import java.util.List;

public interface AccountRepository {
    Account find(AccountNumber accountNumber);

    Account findAccountOfCard(CardNumber cardNumber, LocalDate date);

    List<Account> findAll(Collection<PersonId> owners);

    List<Account> find(BankId bankId, Currency currency);

    List<Account> findByAlias(String alias);

    void update(Account account);

    void create(Account account);
}
