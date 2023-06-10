package ru.vzotov.accounting.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import ru.vzotov.accounting.domain.model.TransactionRepository;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.Transaction;
import ru.vzotov.person.domain.model.PersonId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class TransactionRepositoryJpa extends JpaRepository implements TransactionRepository {

    TransactionRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public Transaction find(OperationId id) {
        try {
            return em.createQuery("from Transaction where primaryOperation = :operationId or secondaryOperation = :operationId", Transaction.class)
                    .setParameter("operationId", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Transaction> findByDate(Collection<PersonId> owners, LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("select tx from Transaction as tx where " +
                                "tx.primaryOperation.id in (select primaryOp.operationId.id from Operation primaryOp, Account a1 where a1.accountNumber=primaryOp.account and a1.owner in (:owners) and (primaryOp.date >= :dateFrom and primaryOp.date <= :dateTo)) " +
                                "or tx.secondaryOperation.id in (select secondaryOp.operationId.id from Operation secondaryOp, Account a2 where a2.accountNumber=secondaryOp.account and a2.owner in (:owners) and (secondaryOp.date >= :dateFrom and secondaryOp.date <= :dateTo))"
                        , Transaction.class)
                .setParameter("owners", owners)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
                .getResultList();
    }

    @Override
    public void store(Transaction transaction) {
        if (em.contains(transaction)) {
            em.detach(transaction);
            em.merge(transaction);
            em.flush();
        } else {
            em.persist(transaction);
        }
    }

    @Override
    public void delete(Transaction transaction) {
        em.remove(transaction);
    }
}
