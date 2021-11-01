package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.BudgetRepository;
import ru.vzotov.accounting.domain.model.Budget;
import ru.vzotov.accounting.domain.model.BudgetId;
import ru.vzotov.accounting.domain.model.BudgetRule;
import ru.vzotov.accounting.domain.model.BudgetRuleId;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class BudgetRepositoryJpa extends JpaRepository implements BudgetRepository {

    BudgetRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public BudgetRule findRule(BudgetRuleId ruleId) {
        try {
            return em.createQuery("from BudgetRule where ruleId.value = :ruleId", BudgetRule.class)
                    .setParameter("ruleId", ruleId.value())
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Budget find(BudgetId budgetId) {
        try {
            return em.createQuery("from Budget where budgetId.value = :budgetId", Budget.class)
                    .setParameter("budgetId", budgetId.value())
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Budget> findAll() {
        return em.createQuery("from Budget", Budget.class).getResultList();
    }

    @Override
    public void store(Budget budget) {
        if (hasId(budget, "id")) {
            //em.detach(budget);
            em.merge(budget);
            em.flush();
        } else {
            em.persist(budget);
        }
    }

    @Override
    public boolean delete(BudgetId budgetId) {
        return em.createQuery("DELETE FROM Budget WHERE budgetId.value = :budgetId")
                .setParameter("budgetId", budgetId.value())
                .executeUpdate() > 0;
    }
}
