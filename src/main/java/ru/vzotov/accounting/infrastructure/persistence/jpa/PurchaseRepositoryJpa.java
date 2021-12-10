package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.purchase.domain.model.Purchase;
import ru.vzotov.purchase.domain.model.PurchaseId;
import ru.vzotov.purchases.domain.model.PurchaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.LocalDateTime;
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
        if (hasId(purchase, "id")) {
            em.detach(purchase);
            em.merge(purchase);
            em.flush();
        } else {
            em.persist(purchase);
        }

    }

    @Override
    public List<Purchase> findByDate(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        return em.createQuery("from Purchase where dateTime >= :fromDate AND dateTime < :toDate", Purchase.class)
                .setParameter("fromDate", fromDateTime)
                .setParameter("toDate", toDateTime)
                .getResultList();
    }

    @Override
    public boolean delete(PurchaseId id) {
        return em.createQuery("DELETE FROM Purchase WHERE purchaseId.value = :id")
                .setParameter("id", id.value())
                .executeUpdate() > 0;
    }
}
