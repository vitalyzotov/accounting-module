package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.AccountRepository;
import ru.vzotov.banking.domain.model.Account;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.BankId;
import ru.vzotov.banking.domain.model.CardNumber;
import ru.vzotov.person.domain.model.PersonId;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Currency;
import java.util.List;

public class AccountRepositoryJpa extends JpaRepository implements AccountRepository {

    AccountRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public Account find(AccountNumber accountNumber) {
        return em.find(Account.class, accountNumber);
    }

    @Override
    public Account findAccountOfCard(CardNumber cardNumber, LocalDate date) {
        try {
            return em.createQuery(
                            """
                                    select a from Account a where a.accountNumber.number in (
                                        select acc.accountNumber.number from Card c
                                        join c.accounts acc 
                                        where c.cardNumber.value=:cardNumber and acc.from<=:date and acc.to>=:date
                                    )
                                    """, Account.class)
                    .setParameter("cardNumber", cardNumber.value())
                    .setParameter("date", date)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Account> findAll(Collection<PersonId> owners) {
        return em.createQuery("from Account where owner in (:owners)", Account.class)
                .setParameter("owners", owners)
                .getResultList();
    }

    @Override
    public List<Account> find(BankId bankId, Currency currency) {
        return em.createQuery("from Account where bankId=:bankId and currency=:currency", Account.class)
                .setParameter("bankId", bankId)
                .setParameter("currency", currency)
                .getResultList();
    }

    @Override
    public List<Account> findByAlias(String alias) {
        return em.createQuery("from Account where :alias member of aliases.value", Account.class)
                .setParameter("alias", alias)
                .getResultList();
    }

    @Override
    public void update(Account account) {
        em.persist(account);
    }

    @Override
    public void create(Account account) {
        Account a = this.find(account.accountNumber());
        if (a == null) {
            em.persist(account);
        } else {
            throw new IllegalStateException("Unable to create account " + account.accountNumber());
        }
    }
}
