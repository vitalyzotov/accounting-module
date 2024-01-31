package ru.vzotov.accounting.domain.model;

import ru.vzotov.person.domain.model.PersonId;

import java.util.List;

public interface PersistentPropertyRepository {
    PersistentProperty find(PersistentPropertyId propertyId);
    PersistentProperty findSystemProperty(String key);
    PersistentProperty findUserProperty(PersonId owner, String key);

    List<PersistentProperty> findAll(PersonId owner);
    List<PersistentProperty> findAllSystemProperties();
    void store(PersistentProperty property);
    boolean delete(PersistentPropertyId propertyId);
}
