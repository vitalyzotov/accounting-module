package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.OperationRepository;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.Operation;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.domain.model.Money;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.LocalDate;
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
    public List<Operation> findByDateAndAmount(LocalDate fromDateInclusive, LocalDate toDateInclusive, Money amount) {
        return em.createQuery("from Operation where date >= :dateFrom and date <= :dateTo and amount = :amount", Operation.class)
                .setParameter("dateFrom", fromDateInclusive)
                .setParameter("dateTo", toDateInclusive)
                .setParameter("amount", amount)
                .getResultList();
    }

    @Override
    public List<Operation> findByDate(LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("from Operation where date >= :dateFrom and date <= :dateTo", Operation.class)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
                .getResultList();
    }

    @Override
    public List<Operation> findByAccountAndDate(AccountNumber accountNumber, LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("from Operation where account.accountNumber.number = :accountNumber and date >= :dateFrom and date <= :dateTo", Operation.class)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
                .setParameter("accountNumber", accountNumber.number())
                .getResultList();
    }

    @Override
    public List<Operation> findByTypeAndDate(OperationType type, LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("from Operation where type = :type and date >= :dateFrom and date <= :dateTo", Operation.class)
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
}
