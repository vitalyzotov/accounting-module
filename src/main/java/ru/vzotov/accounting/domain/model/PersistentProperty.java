package ru.vzotov.accounting.domain.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import ru.vzotov.ddd.shared.AggregateRoot;
import ru.vzotov.ddd.shared.Entity;
import ru.vzotov.person.domain.model.Owned;
import ru.vzotov.person.domain.model.PersonId;

import java.time.Instant;
import java.util.Objects;

@AggregateRoot
public class PersistentProperty implements Entity<PersistentProperty>, Owned {
    private PersistentPropertyId propertyId;

    private String key;

    private String value;

    private PersonId owner;

    private Instant createdOn;

    private Instant updatedOn;

    public PersistentProperty(PersistentPropertyId propertyId, String key) {
        this(propertyId, key, (String) null);
    }

    public PersistentProperty(PersistentPropertyId propertyId, String key, PersonId owner) {
        this(propertyId, key, null, owner);
    }

    /**
     * Create system property
     */
    public PersistentProperty(PersistentPropertyId propertyId, String key, String value) {
        this(propertyId, key, value, null);
    }

    /**
     * Create user property
     */
    public PersistentProperty(PersistentPropertyId propertyId, String key, String value, PersonId owner) {
        this.propertyId = Objects.requireNonNull(propertyId);
        this.key = Objects.requireNonNull(key);
        this.value = value;
        this.owner = owner;
    }

    public PersistentPropertyId propertyId() {
        return propertyId;
    }

    public String key() {
        return key;
    }

    public String value() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Instant createdOn() {
        return createdOn;
    }

    public Instant updatedOn() {
        return updatedOn;
    }

    @Override
    public PersonId owner() {
        return owner;
    }

    @Override
    public boolean sameIdentityAs(PersistentProperty other) {
        return other != null && new EqualsBuilder()
                .append(propertyId, other.propertyId)
                .isEquals();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PersistentProperty that = (PersistentProperty) o;
        return sameIdentityAs(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyId);
    }

    protected PersistentProperty() {
        //for Hibernate
    }

    protected void onCreate() {
        createdOn = Instant.now();
        updatedOn = createdOn;
    }

    protected void onUpdate() {
        updatedOn = Instant.now();
    }

    /**
     * Surrogate key
     */
    private Long id;
}
