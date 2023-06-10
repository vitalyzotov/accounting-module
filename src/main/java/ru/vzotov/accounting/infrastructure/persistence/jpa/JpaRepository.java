package ru.vzotov.accounting.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;

public abstract class JpaRepository {

    protected final EntityManager em;

    public JpaRepository(EntityManager em) {
        this.em = em;
    }

}
