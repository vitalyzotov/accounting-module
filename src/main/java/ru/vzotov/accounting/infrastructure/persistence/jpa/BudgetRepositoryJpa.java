package ru.vzotov.accounting.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import ru.vzotov.accounting.domain.model.Budget;
import ru.vzotov.accounting.domain.model.BudgetId;
import ru.vzotov.accounting.domain.model.BudgetRepository;
import ru.vzotov.accounting.domain.model.BudgetRule;
import ru.vzotov.accounting.domain.model.BudgetRuleId;
import ru.vzotov.person.domain.model.PersonId;

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
    public Budget findForRule(BudgetRuleId ruleId) {
        try {
            return em.createQuery("from Budget where (select rule from BudgetRule rule where ruleId=:ruleId) member of rules", Budget.class)
                    .setParameter("ruleId", ruleId)
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
    public List<Budget> find(PersonId owner) {
        return em.createQuery("from Budget where owner = :owner", Budget.class)
                .setParameter("owner", owner)
                .getResultList();
    }

    @Override
    public List<Budget> findAll() {
        return em.createQuery("from Budget", Budget.class).getResultList();
    }

    @Override
    public void store(Budget budget) {
        if (em.contains(budget)) {
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
