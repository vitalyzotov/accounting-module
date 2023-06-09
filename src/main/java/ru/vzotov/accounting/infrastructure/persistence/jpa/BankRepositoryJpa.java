package ru.vzotov.accounting.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import ru.vzotov.accounting.domain.model.BankRepository;
import ru.vzotov.banking.domain.model.Bank;
import ru.vzotov.banking.domain.model.BankId;

import java.util.List;

public class BankRepositoryJpa extends JpaRepository implements BankRepository {

    BankRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public Bank find(BankId bankId) {
        try {
            return em.createQuery("from Bank where bankId.value = :bankId", Bank.class)
                    .setParameter("bankId", bankId.value())
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Bank> findAll() {
        return em.createQuery("from Bank", Bank.class).getResultList();
    }

    @Override
    public void store(Bank bank) {
        if (em.contains(bank)) {
            em.detach(bank);
            em.merge(bank);
            em.flush();
        } else {
            em.persist(bank);
        }
    }

    @Override
    public void delete(Bank bank) {
        em.remove(bank);
    }
}
