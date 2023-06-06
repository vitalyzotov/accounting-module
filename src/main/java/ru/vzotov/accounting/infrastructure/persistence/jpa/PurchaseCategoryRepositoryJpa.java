package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategory;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryRepository;
import ru.vzotov.person.domain.model.PersonId;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class PurchaseCategoryRepositoryJpa extends JpaRepository implements PurchaseCategoryRepository {

    private static final Logger log = LoggerFactory.getLogger(PurchaseCategoryRepositoryJpa.class);

    PurchaseCategoryRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public PurchaseCategory findById(PurchaseCategoryId id) {
        try {
            return em.createQuery("from PurchaseCategory where categoryId = :id", PurchaseCategory.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public PurchaseCategory findByName(PersonId owner, String name) {
        try {
            return em.createQuery("from PurchaseCategory where owner=:owner and name = :name", PurchaseCategory.class)
                    .setParameter("owner", owner)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<PurchaseCategory> findAll(PersonId owner) {
        return em.createQuery("from PurchaseCategory where owner=:owner", PurchaseCategory.class)
                .setParameter("owner", owner)
                .getResultList();
    }

    @Override
    public void store(PurchaseCategory category) {
        if (em.contains(category)) {
            em.merge(category);
            em.flush();
        } else {
            em.persist(category);
        }
    }
}
