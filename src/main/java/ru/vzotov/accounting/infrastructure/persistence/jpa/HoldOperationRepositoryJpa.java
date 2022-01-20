package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.HoldOperationRepository;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.HoldId;
import ru.vzotov.banking.domain.model.HoldOperation;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.domain.model.Money;
import ru.vzotov.person.domain.model.PersonId;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class HoldOperationRepositoryJpa extends JpaRepository implements HoldOperationRepository {

    HoldOperationRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public HoldOperation find(HoldId id) {
        try {
            return em.createQuery("from HoldOperation where holdId = :id", HoldOperation.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public HoldOperation find(AccountNumber account, LocalDate date, Money amount, OperationType type, String description) {
        try {
            return em.createQuery("from HoldOperation" +
                            " where account=:accountNumber" +
                            " and date=:date" +
                            " and amount.amount=:amount" +
                            " and amount.currency=:currency" +
                            " and type=:type" +
                            " and description=:description", HoldOperation.class)
                    .setParameter("accountNumber", account)
                    .setParameter("date", date)
                    .setParameter("amount", amount.rawAmount())
                    .setParameter("currency", amount.currency())
                    .setParameter("type", type)
                    .setParameter("description", description)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<HoldOperation> findByAccountAndDate(AccountNumber accountNumber, LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("from HoldOperation" +
                        " where account=:accountNumber" +
                        " and date >= :dateFrom" +
                        " and date <= :dateTo", HoldOperation.class)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
                .setParameter("accountNumber", accountNumber)
                .getResultList();
    }

    @Override
    public List<HoldOperation> findByDate(Collection<PersonId> owners, LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("select op from HoldOperation op, Account a" +
                        " where op.date >= :dateFrom" +
                        " and op.date <= :dateTo" +
                        " and a.accountNumber=op.account" +
                        " and a.owner in (:owners)", HoldOperation.class)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
                .setParameter("owners", owners)
                .getResultList();
    }

    @Override
    public List<HoldOperation> findByTypeAndDate(Collection<PersonId> owners, OperationType type, LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("select op from HoldOperation op, Account a" +
                        " where op.type = :type" +
                        " and op.date >= :dateFrom" +
                        " and op.date <= :dateTo" +
                        " and op.account=a.accountNumber" +
                        " and a.owner in (:owners)", HoldOperation.class)
                .setParameter("type", type)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
                .setParameter("owners", owners)
                .getResultList();
    }

    @Override
    public void store(HoldOperation operation) {
        if (hasId(operation, "id")) {
            em.detach(operation);
            em.merge(operation);
            em.flush();
        } else {
            em.persist(operation);
        }
    }

    @Override
    public void delete(HoldOperation operation) {
        em.remove(operation);
    }

}
