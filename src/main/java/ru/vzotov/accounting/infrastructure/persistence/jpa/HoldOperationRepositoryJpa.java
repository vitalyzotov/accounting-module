package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.HoldOperationRepository;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.HoldId;
import ru.vzotov.banking.domain.model.HoldOperation;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.domain.model.Money;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.List;

public class HoldOperationRepositoryJpa extends JpaRepository implements HoldOperationRepository {

    HoldOperationRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public HoldOperation find(HoldId id) {
        try {
            return em.createQuery("from HoldOperation where holdId.value = :id", HoldOperation.class)
                    .setParameter("id", id.value())
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public HoldOperation find(AccountNumber account, LocalDate date, Money amount, OperationType type, String description) {
        try {
            return em.createQuery("from HoldOperation" +
                            " where account.accountNumber.number = :accountNumber" +
                            " and date=:date" +
                            " and amount.amount=:amount" +
                            " and amount.currency=:currency" +
                            " and type=:type" +
                            " and description=:description", HoldOperation.class)
                    .setParameter("accountNumber", account.number())
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
        return em.createQuery("from HoldOperation where account.accountNumber.number = :accountNumber and date >= :dateFrom and date <= :dateTo", HoldOperation.class)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
                .setParameter("accountNumber", accountNumber.number())
                .getResultList();
    }

    @Override
    public List<HoldOperation> findByDate(LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("from HoldOperation where date >= :dateFrom and date <= :dateTo", HoldOperation.class)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
                .getResultList();
    }

    @Override
    public List<HoldOperation> findByTypeAndDate(OperationType type, LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("from HoldOperation where type = :type and date >= :dateFrom and date <= :dateTo", HoldOperation.class)
                .setParameter("type", type)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
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
