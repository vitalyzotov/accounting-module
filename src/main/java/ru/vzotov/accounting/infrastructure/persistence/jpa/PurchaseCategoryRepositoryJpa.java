package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategory;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.lang.reflect.Field;
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
    public PurchaseCategory findByName(String name) {
        try {
            return em.createQuery("from PurchaseCategory where name = :name", PurchaseCategory.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<PurchaseCategory> findAll() {
        return em.createQuery("from PurchaseCategory", PurchaseCategory.class).getResultList();
    }

    @Override
    public void store(PurchaseCategory category) {
        try {
            Field f = PurchaseCategory.class.getDeclaredField("id");
            f.setAccessible(true);
            Object val = f.get(category);
            if (val != null) {
                em.merge(category);
                em.flush();
            } else {
                em.persist(category);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("error", e);
        }
    }
}
