package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.Deal;
import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.domain.model.DealRepository;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.person.domain.model.PersonId;
import ru.vzotov.purchase.domain.model.PurchaseId;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.trimToNull;

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
    public List<Deal> findByDate(Collection<PersonId> owners, String query, LocalDate fromDate, LocalDate toDate) {
        //todo: use fulltext search engine
        return em.createQuery("""
                        from Deal d where d.owner in (:owners)
                         AND (:query IS NULL OR (
                            LOCATE(:query, LOWER(d.description))>0
                         OR LOCATE(:query, LOWER(d.comment))>0
                         OR EXISTS(SELECT 1 FROM Purchase p WHERE p.purchaseId member of d.purchases AND LOCATE(:query, LOWER(p.name))>0)
                         OR EXISTS(SELECT 1 FROM Operation o WHERE o.operationId member of d.operations AND LOCATE(:query, LOWER(o.description))>0)
                         OR EXISTS(SELECT 1 FROM Receipt r WHERE r.receiptId member of d.receipts AND (
                                LOCATE(:query, LOWER(r.shiftInfo.operator))>0
                             OR LOCATE(:query, LOWER(r.retailPlace.user))>0
                             OR LOCATE(:query, LOWER(r.retailPlace.address.value))>0
                           )
                         )
                        )) AND d.date >= :dateFrom
                         AND d.date <= :dateTo
                        """, Deal.class)
                .setParameter("owners", owners)
                .setParameter("dateFrom", fromDate)
                .setParameter("dateTo", toDate)
                .setParameter("query", lowerCase(trimToNull(query)))
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
        if (em.contains(deal)) {
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
    public LocalDate findMinDealDate(Collection<PersonId> owners) {
        try {
            return em.createQuery("select min(date) from Deal where owner in (:owners)", LocalDate.class)
                    .setParameter("owners", owners)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public LocalDate findMaxDealDate(Collection<PersonId> owners) {
        try {
            return em.createQuery("select max(date) from Deal where owner in (:owners)", LocalDate.class)
                    .setParameter("owners", owners)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public LocalDate[] findMinMaxDealDates(Collection<PersonId> owners) {
        try {
            Object[] result = (Object[]) em.createQuery("select min(date), max(date) from Deal where owner in (:owners)")
                    .setParameter("owners", owners)
                    .getSingleResult();
            return new LocalDate[]{
                    (LocalDate) result[0], (LocalDate) result[1]
            };
        } catch (NoResultException ex) {
            return null;
        }
    }
}
