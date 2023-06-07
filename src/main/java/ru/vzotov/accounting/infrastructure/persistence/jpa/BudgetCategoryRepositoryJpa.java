package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.BudgetCategoryRepository;
import ru.vzotov.banking.domain.model.BudgetCategory;
import ru.vzotov.banking.domain.model.BudgetCategoryId;
import ru.vzotov.person.domain.model.PersonId;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

public class BudgetCategoryRepositoryJpa extends JpaRepository implements BudgetCategoryRepository {

    BudgetCategoryRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public BudgetCategory find(BudgetCategoryId id) {
        return em.find(BudgetCategory.class, id);
    }

    @Override
    public BudgetCategory find(PersonId owner, String categoryName) {
        try {
            return em.createQuery("from BudgetCategory where owner=:owner and name = :name", BudgetCategory.class)
                    .setParameter("owner", owner)
                    .setParameter("name", categoryName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<BudgetCategory> findAll(PersonId owner) {
        return em.createQuery("from BudgetCategory where owner=:owner", BudgetCategory.class)
                .setParameter("owner", owner)
                .getResultList();
    }

    @Override
    public void store(BudgetCategory budgetCategory) {
        em.persist(budgetCategory);
    }

    @Override
    public void delete(BudgetCategory category) {
        em.remove(category);
        em.flush();
    }
}
