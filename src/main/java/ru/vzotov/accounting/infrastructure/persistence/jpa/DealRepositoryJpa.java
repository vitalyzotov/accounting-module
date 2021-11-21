package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.Deal;
import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.domain.model.DealRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.List;

public class DealRepositoryJpa extends JpaRepository implements DealRepository {

    DealRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public Deal find(DealId dealId) {
        try {
            return em.createQuery("from Deal where dealId.value = :dealId", Deal.class)
                    .setParameter("dealId", dealId.value())
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
    public void store(Deal deal) {
        em.persist(deal);
    }

    @Override
    public void delete(Deal deal) {
        em.remove(deal);
        em.flush();
    }

    @Override
    public LocalDate findMinDealDate() {
        try {
            return em.createQuery("select min(date) from Deal", LocalDate.class).getSingleResult();
        }catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public LocalDate findMaxDealDate() {
        try {
            return em.createQuery("select max(date) from Deal", LocalDate.class).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}