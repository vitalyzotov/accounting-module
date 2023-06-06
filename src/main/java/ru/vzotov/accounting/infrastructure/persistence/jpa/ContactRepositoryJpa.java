package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vzotov.accounting.domain.model.ContactRepository;
import ru.vzotov.person.domain.model.Contact;
import ru.vzotov.person.domain.model.ContactId;
import ru.vzotov.person.domain.model.PersonId;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class ContactRepositoryJpa extends JpaRepository implements ContactRepository {

    private static final Logger log = LoggerFactory.getLogger(ContactRepositoryJpa.class);

    public ContactRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public Contact find(ContactId contactId) {
        try {
            return em.createQuery("from Contact where contactId = :contactId", Contact.class)
                    .setParameter("contactId", contactId)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }

    }

    @Override
    public List<Contact> find(PersonId owner) {
        return em.createQuery("from Contact where owner = :owner", Contact.class)
                .setParameter("owner", owner)
                .getResultList();
    }

    @Override
    public void store(Contact contact) {
        if (em.contains(contact)) {
            em.merge(contact);
            em.flush();
        } else {
            em.persist(contact);
        }
    }

    @Override
    public boolean delete(ContactId contactId) {
        return em.createQuery("DELETE FROM Contact WHERE contactId = :contactId")
                .setParameter("contactId", contactId)
                .executeUpdate() > 0;
    }
}
