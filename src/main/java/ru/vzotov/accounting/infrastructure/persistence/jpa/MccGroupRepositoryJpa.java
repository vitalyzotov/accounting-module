package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.MccGroupRepository;
import ru.vzotov.banking.domain.model.MccGroup;
import ru.vzotov.banking.domain.model.MccGroupId;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

public class MccGroupRepositoryJpa extends JpaRepository implements MccGroupRepository {

    MccGroupRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public MccGroup find(MccGroupId id) {
        try {
            return em.createQuery("from MccGroup where groupId = :groupId", MccGroup.class)
                    .setParameter("groupId", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<MccGroup> findAll() {
        return em.createQuery("from MccGroup", MccGroup.class)
                .getResultList();
    }
}
