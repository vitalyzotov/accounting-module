package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vzotov.accounting.domain.model.Remain;
import ru.vzotov.accounting.domain.model.RemainId;
import ru.vzotov.accounting.domain.model.RemainRepository;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.person.domain.model.PersonId;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class RemainRepositoryJpa extends JpaRepository implements RemainRepository {

    private static final Logger log = LoggerFactory.getLogger(RemainRepositoryJpa.class);

    RemainRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public Remain find(RemainId id) {
        try {
            log.debug("Search for purchase by id {}", id);
            return em.createQuery("from Remain where remainId = :id", Remain.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }

    }

    @Override
    public List<Remain> find(Collection<PersonId> owners, Collection<AccountNumber> accounts, LocalDate fromDate, LocalDate toDate, boolean recentOnly) {
        return em.createNamedQuery("recent-remains", Remain.class)
                .setParameter(1, owners.stream().map(PersonId::value).collect(Collectors.toList()))
                .setParameter(2, accounts == null ? null : accounts.stream().map(AccountNumber::number).collect(Collectors.toList()))
                .setParameter(3, fromDate)
                .setParameter(4, toDate)
                .setParameter(5, recentOnly)
                .setParameter(6, accounts == null)
                .getResultList();
    }

    @Override
    public List<Remain> findByDate(Collection<PersonId> owners, LocalDate fromDate, LocalDate toDate) {
        return em.createQuery(
                        "select r from Remain r, Account a where a.owner in (:owners) " +
                                "and r.account=a.accountNumber and r.date >= :dateFrom and r.date <= :dateTo",
                        Remain.class
                )
                .setParameter("owners", owners)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
                .getResultList();
    }

    @Override
    public List<Remain> findByAccountNumber(AccountNumber accountNumber) {
        return em.createQuery("from Remain where account = :accountNumber", Remain.class)
                .setParameter("accountNumber", accountNumber)
                .getResultList();
    }

    @Override
    public void store(Remain remain) {
        if (hasId(remain, "id")) {
            em.detach(remain);
            em.merge(remain);
            em.flush();
        } else {
            em.persist(remain);
        }
    }

    @Override
    public void delete(Remain remain) {
        em.remove(remain);
    }
}
