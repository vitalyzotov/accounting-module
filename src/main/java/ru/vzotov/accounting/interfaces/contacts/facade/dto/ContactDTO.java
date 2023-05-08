package ru.vzotov.accounting.interfaces.contacts.facade.dto;

import java.time.OffsetDateTime;
import java.util.List;

public class ContactDTO {

    private String id;

    private long version;

    private String owner;

    private String firstName;

    private String middleName;

    private String lastName;

    private String displayName;

    private List<ContactDataDTO> data;

    private OffsetDateTime created;

    private OffsetDateTime updated;

    public ContactDTO() {
    }

    public ContactDTO(String id, long version, String owner, String firstName, String middleName, String lastName,
                      String displayName, List<ContactDataDTO> data, OffsetDateTime created, OffsetDateTime updated) {
        this.id = id;
        this.version = version;
        this.owner = owner;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.data = data;
        this.created = created;
        this.updated = updated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<ContactDataDTO> getData() {
        return data;
    }

    public void setData(List<ContactDataDTO> data) {
        this.data = data;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    public OffsetDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(OffsetDateTime updated) {
        this.updated = updated;
    }
}
