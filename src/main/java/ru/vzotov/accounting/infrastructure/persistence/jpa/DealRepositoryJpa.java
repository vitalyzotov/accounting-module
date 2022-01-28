package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.Deal;
import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.domain.model.DealRepository;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.person.domain.model.PersonId;
import ru.vzotov.purchase.domain.model.PurchaseId;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class DealRepositoryJpa extends JpaRepository implements DealRepository {

    DealRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public Deal find(DealId dealId) {
        try {
            return em.createQuery("from Deal where dealId = :dealId", Deal.class)
                    .setParameter("dealId", dealId)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Deal> findByDate(LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("from Deal where date >= :dateFrom and date <= :dateTo", Deal.class)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
                .getResultList();
    }

    @Override
    public List<Deal> findByDate(Collection<PersonId> owners, LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("from Deal where owner in (:owners) and date >= :dateFrom and date <= :dateTo", Deal.class)
                .setParameter("owners", owners)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
                .getResultList();
    }

    @Override
    public Deal findByOperation(OperationId operation) {
        try {
            return em.createQuery("from Deal where :operationId member of operations", Deal.class)
                    .setParameter("operationId", operation)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Deal findByReceipt(ReceiptId receiptId) {
        try {
            return em.createQuery("from Deal where :receipt member of receipts", Deal.class)
                    .setParameter("receipt", receiptId)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Deal findByPurchase(PurchaseId purchaseId) {
        try {
            return em.createQuery("from Deal where :purchase member of purchases", Deal.class)
                    .setParameter("purchase", purchaseId)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void store(Deal deal) {
        if (hasId(deal, "id")) {
            em.merge(deal);
        } else {
            em.persist(deal);
        }
    }

    @Override
    public void delete(Deal deal) {
        em.remove(deal);
    }

    @Override
    public LocalDate findMinDealDate(PersonId owner) {
        try {
            return em.createQuery("select min(date) from Deal where owner=:owner", LocalDate.class)
                    .setParameter("owner", owner)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public LocalDate findMaxDealDate(PersonId owner) {
        try {
            return em.createQuery("select max(date) from Deal where owner=:owner", LocalDate.class)
                    .setParameter("owner", owner)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
