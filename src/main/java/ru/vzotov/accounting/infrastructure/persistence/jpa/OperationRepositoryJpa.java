package ru.vzotov.accounting.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import ru.vzotov.accounting.domain.model.OperationRepository;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.Operation;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.domain.model.Money;
import ru.vzotov.person.domain.model.PersonId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class OperationRepositoryJpa extends JpaRepository implements OperationRepository {

    OperationRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public Operation find(OperationId operationId) {
        try {
            return em.createQuery("from Operation where operationId = :operationId", Operation.class)
                    .setParameter("operationId", operationId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Operation> findByDateAndAmount(Collection<PersonId> owners, LocalDate fromDateInclusive, LocalDate toDateInclusive, Money amount) {
        return em.createQuery("select op from Operation op, Account a where a.accountNumber=op.account and a.owner in (:owners) and op.date >= :dateFrom and op.date <= :dateTo and op.amount = :amount", Operation.class)
                .setParameter("owners", owners)
                .setParameter("dateFrom", fromDateInclusive)
                .setParameter("dateTo", toDateInclusive)
                .setParameter("amount", amount)
                .getResultList();
    }

    @Override
    public List<Operation> findByDate(Collection<PersonId> owners, LocalDate fromDate, LocalDate toDate) {
        //fixme: implement security
        return em.createQuery("select op from Operation op, Account a where a.accountNumber=op.account and a.owner in (:owners) and op.date >= :dateFrom and op.date <= :dateTo", Operation.class)
                .setParameter("owners", owners)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
                .getResultList();
    }

    @Override
    public List<Operation> findByAccountAndDate(AccountNumber accountNumber, LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("from Operation where account = :accountNumber and date >= :dateFrom and date <= :dateTo", Operation.class)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
                .setParameter("accountNumber", accountNumber)
                .getResultList();
    }

    @Override
    public List<Operation> findByTypeAndDate(Collection<PersonId> owners, OperationType type, LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("select op from Operation op, Account a where a.accountNumber=op.account and a.owner in (:owners) and op.type = :type and op.date >= :dateFrom and op.date <= :dateTo", Operation.class)
                .setParameter("owners", owners)
                .setParameter("type", type)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
                .getResultList();
    }

    @Override
    public void store(Operation operation) {
        em.persist(operation);
    }

    @Override
    public void delete(Operation operation) {
        em.remove(operation);
        em.flush();
    }

    @Override
    public List<Operation> findWithoutDeals() {
        return em.createNamedQuery("operations-without-deals", Operation.class)
                .getResultList();
    }
}
