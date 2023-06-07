package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.CardOperationRepository;
import ru.vzotov.banking.domain.model.CardOperation;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.domain.model.Money;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.time.LocalDate;
import java.util.List;

public class CardOperationRepositoryJpa extends JpaRepository implements CardOperationRepository {

    CardOperationRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public CardOperation find(OperationId operationId) {
        try {
            return em.createQuery("from CardOperation where operationId = :operationId", CardOperation.class)
                    .setParameter("operationId", operationId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<CardOperation> findByDateAndAmount(LocalDate fromDateInclusive, LocalDate toDateInclusive, Money amount) {
        return em.createQuery("from CardOperation where purchaseDate >= :dateFrom and purchaseDate <= :dateTo and amount = :amount", CardOperation.class)
                .setParameter("dateFrom", fromDateInclusive)
                .setParameter("dateTo", toDateInclusive)
                .setParameter("amount", amount)
                .getResultList();
    }

    @Override
    public List<CardOperation> findByDate(LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("from CardOperation where purchaseDate >= :dateFrom and purchaseDate <= :dateTo", CardOperation.class)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
                .getResultList();
    }

    @Override
    public void store(CardOperation operation) {
        em.persist(operation);
    }
}
