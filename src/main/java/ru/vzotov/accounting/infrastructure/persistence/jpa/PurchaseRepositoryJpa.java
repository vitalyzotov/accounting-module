package ru.vzotov.accounting.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vzotov.person.domain.model.PersonId;
import ru.vzotov.purchase.domain.model.Purchase;
import ru.vzotov.purchase.domain.model.PurchaseId;
import ru.vzotov.purchases.domain.model.PurchaseRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class PurchaseRepositoryJpa extends JpaRepository implements PurchaseRepository {

    private static final Logger log = LoggerFactory.getLogger(PurchaseRepositoryJpa.class);

    PurchaseRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public Purchase find(PurchaseId id) {
        try {
            log.debug("Search for purchase by id {}", id);
            return em.createQuery("from Purchase where purchaseId.value = :id", Purchase.class)
                    .setParameter("id", id.value())
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void store(Purchase purchase) {
        if (em.contains(purchase)) {
            em.detach(purchase);
            em.merge(purchase);
            em.flush();
        } else {
            em.persist(purchase);
        }

    }

    @Override
    public List<Purchase> findByDate(Collection<PersonId> owners, LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        return em.createQuery("from Purchase where owner in (:owners) AND dateTime >= :fromDate AND dateTime < :toDate", Purchase.class)
                .setParameter("owners", owners)
                .setParameter("fromDate", fromDateTime)
                .setParameter("toDate", toDateTime)
                .getResultList();
    }

    @Override
    public boolean delete(PurchaseId id) {
        final Purchase p = find(id);
        if (p != null) {
            em.remove(p);
            return true;
        } else {
            return false;
        }
    }
}
