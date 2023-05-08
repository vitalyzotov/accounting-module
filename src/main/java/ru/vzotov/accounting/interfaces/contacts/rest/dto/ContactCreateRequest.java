package ru.vzotov.accounting.interfaces.contacts.rest.dto;

import ru.vzotov.accounting.interfaces.contacts.facade.dto.ContactDataDTO;

import java.util.List;

public class ContactCreateRequest {
    private String firstName;

    private String middleName;

    private String lastName;

    private String displayName;

    private List<ContactDataDTO> data;

    public ContactCreateRequest() {
    }

    public ContactCreateRequest(String firstName, String middleName, String lastName, String displayName, List<ContactDataDTO> data) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.data = data;
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
}
