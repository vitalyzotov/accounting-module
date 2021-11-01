package ru.vzotov.accounting.infrastructure.persistence.jpa;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class JpaRepository {

    @SuppressWarnings("WeakerAccess")
    protected final EntityManager em;

    public JpaRepository(EntityManager em) {
        this.em = em;
    }

    protected boolean hasId(Object entity, String idFieldName) {
        Object id = null;
        try {
            Field field = entity.getClass().getDeclaredField(idFieldName);
            if (Modifier.isPrivate(field.getModifiers())) {
                field.setAccessible(true);
                id = field.get(entity);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
        return id != null;
    }

}
