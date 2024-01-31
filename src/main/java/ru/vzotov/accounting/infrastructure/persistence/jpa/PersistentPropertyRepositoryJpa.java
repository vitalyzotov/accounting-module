package ru.vzotov.accounting.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vzotov.accounting.domain.model.PersistentProperty;
import ru.vzotov.accounting.domain.model.PersistentPropertyId;
import ru.vzotov.accounting.domain.model.PersistentPropertyRepository;
import ru.vzotov.person.domain.model.PersonId;

import java.util.List;

public class PersistentPropertyRepositoryJpa extends JpaRepository implements PersistentPropertyRepository {

    private static final Logger log = LoggerFactory.getLogger(PersistentPropertyRepositoryJpa.class);

    PersistentPropertyRepositoryJpa(EntityManager em) {
        super(em);
    }


    @Override
    public PersistentProperty find(PersistentPropertyId id) {
        try {
            return em.createQuery("from PersistentProperty where propertyId.value = :id", PersistentProperty.class)
                    .setParameter("id", id.value())
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public PersistentProperty findSystemProperty(String key) {
        try {
            return em.createQuery("from PersistentProperty where key = :key and owner is null", PersistentProperty.class)
                    .setParameter("key", key)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public PersistentProperty findUserProperty(PersonId owner, String key) {
        try {
            return em.createQuery("from PersistentProperty where key = :key and owner = :owner", PersistentProperty.class)
                    .setParameter("key", key)
                    .setParameter("owner", owner)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<PersistentProperty> findAll(PersonId owner) {
        return em.createQuery("from PersistentProperty where owner = :owner", PersistentProperty.class)
                .setParameter("owner", owner)
                .getResultList();
    }

    @Override
    public List<PersistentProperty> findAllSystemProperties() {
        return em.createQuery("from PersistentProperty where owner is null", PersistentProperty.class)
                .getResultList();
    }

    @Override
    public void store(PersistentProperty property) {
        if (em.contains(property)) {
            em.detach(property);
            em.merge(property);
            em.flush();
        } else {
            em.persist(property);
        }

    }

    @Override
    public boolean delete(PersistentPropertyId propertyId) {
        final PersistentProperty p = find(propertyId);
        if (p != null) {
            em.remove(p);
            return true;
        } else {
            return false;
        }
    }
}
