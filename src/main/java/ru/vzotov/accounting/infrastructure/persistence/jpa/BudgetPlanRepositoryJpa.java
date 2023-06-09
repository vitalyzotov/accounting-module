package ru.vzotov.accounting.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import ru.vzotov.accounting.domain.model.BudgetPlan;
import ru.vzotov.accounting.domain.model.BudgetPlanId;
import ru.vzotov.accounting.domain.model.BudgetPlanRepository;
import ru.vzotov.accounting.domain.model.BudgetRuleId;

import java.util.List;

public class BudgetPlanRepositoryJpa extends JpaRepository implements BudgetPlanRepository {

    BudgetPlanRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public BudgetPlan find(BudgetPlanId itemId) {
        try {
            return em.createQuery("from BudgetPlan where itemId.value = :itemId", BudgetPlan.class)
                    .setParameter("itemId", itemId.value())
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<BudgetPlan> findForRule(BudgetRuleId ruleId) {
        return em.createQuery("from BudgetPlan where rule.ruleId.value = :ruleId", BudgetPlan.class)
                .setParameter("ruleId", ruleId.value())
                .getResultList();

    }

    @Override
    public List<BudgetPlan> findAll() {
        return em.createQuery("from BudgetPlan", BudgetPlan.class).getResultList();
    }

    @Override
    public void store(BudgetPlan item) {
        if (em.contains(item)) {
            //em.detach(item);
            em.merge(item);
            em.flush();
        } else {
            em.persist(item);
        }
    }

    @Override
    public boolean delete(BudgetPlanId itemId) {
        return em.createQuery("DELETE FROM BudgetPlan WHERE itemId.value = :itemId")
                .setParameter("itemId", itemId.value())
                .executeUpdate() > 0;
    }
}
