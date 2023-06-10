package ru.vzotov.accounting.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import ru.vzotov.accounting.domain.model.CardRepository;
import ru.vzotov.banking.domain.model.BankId;
import ru.vzotov.banking.domain.model.Card;
import ru.vzotov.banking.domain.model.CardNumber;
import ru.vzotov.person.domain.model.PersonId;

import java.util.List;

public class CardRepositoryJpa extends JpaRepository implements CardRepository {

    CardRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public Card find(CardNumber cardNumber) {
        try {
            return em.createQuery("from Card where cardNumber.value = :number", Card.class)
                    .setParameter("number", cardNumber.value())
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Card> find(PersonId owner) {
        return em.createQuery("from Card where owner = :owner", Card.class)
                .setParameter("owner", owner)
                .getResultList();
    }

    @Override
    public List<Card> findByMask(String numberMask) {
        final String mask = numberMask.replace('*', '%').replace('+', '_');
        return em.createQuery("from Card where cardNumber.value like :number", Card.class)
                .setParameter("number", mask)
                .getResultList();
    }

    @Override
    public List<Card> findAll() {
        return em.createQuery("from Card", Card.class).getResultList();
    }

    @Override
    public List<Card> findByBank(PersonId owner, BankId bankId) {
        return em.createQuery("from Card where owner = :owner and issuer = :issuer", Card.class)
                .setParameter("owner", owner)
                .setParameter("issuer", bankId)
                .getResultList();
    }

    @Override
    public void store(Card card) {
        if (em.contains(card)) {
            em.detach(card);
            em.merge(card);
            em.flush();
        } else {
            em.persist(card);
        }
    }

    @Override
    public void delete(Card card) {
        em.remove(card);
    }
}
