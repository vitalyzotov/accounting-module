package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.CardRepository;
import ru.vzotov.banking.domain.model.BankId;
import ru.vzotov.banking.domain.model.Card;
import ru.vzotov.banking.domain.model.CardNumber;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
    public List<Card> findByBank(BankId bankId) {
        return em.createQuery("from Card where issuer.value = :issuer", Card.class)
                .setParameter("issuer", bankId.value())
                .getResultList();
    }

    @Override
    public void store(Card card) {
        if (hasId(card, "id")) {
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
