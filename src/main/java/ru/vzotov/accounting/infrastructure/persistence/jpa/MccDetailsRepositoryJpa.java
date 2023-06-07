package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.MccDetailsRepository;
import ru.vzotov.banking.domain.model.MccCode;
import ru.vzotov.banking.domain.model.MccDetails;
import ru.vzotov.banking.domain.model.MccGroupId;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

public class MccDetailsRepositoryJpa extends JpaRepository implements MccDetailsRepository {

    MccDetailsRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public MccDetails find(MccCode code) {
        try {
            return em.createQuery("from MccDetails d left join fetch d.group where d.mcc = :mcc", MccDetails.class)
                    .setParameter("mcc", code)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<MccDetails> findByGroup(MccGroupId groupId) {
        return em.createQuery("from MccDetails d left join fetch d.group g where d.group.groupId = :groupId", MccDetails.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }

    @Override
    public List<MccDetails> findAll() {
        return em.createQuery("from MccDetails d left join fetch d.group", MccDetails.class)
                .getResultList();
    }
}
