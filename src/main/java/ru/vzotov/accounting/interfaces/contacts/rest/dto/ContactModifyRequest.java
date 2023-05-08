package ru.vzotov.accounting.interfaces.contacts.rest.dto;

import ru.vzotov.accounting.interfaces.contacts.facade.dto.ContactDataDTO;

import java.util.List;

public class ContactModifyRequest extends ContactCreateRequest {

    private long version;

    public ContactModifyRequest() {
    }

    public ContactModifyRequest(long version) {
        this.version = version;
    }

    public ContactModifyRequest(long version, String firstName, String middleName, String lastName, String displayName, List<ContactDataDTO> data) {
        super(firstName, middleName, lastName, displayName, data);
        this.version = version;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
