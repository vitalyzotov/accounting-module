package ru.vzotov.accounting.interfaces.contacts.facade;

import ru.vzotov.accounting.interfaces.common.EntityConflictException;
import ru.vzotov.accounting.interfaces.contacts.ContactsApi.Contact;
import ru.vzotov.accounting.interfaces.contacts.ContactsApi.ContactData;
import ru.vzotov.person.domain.model.ContactId;

import java.util.Collection;
import java.util.List;

public interface ContactsFacade {

    List<Contact> listContacts();

    Contact getContact(ContactId contactId) throws ContactNotFoundException;

    Contact deleteContact(ContactId contactId) throws ContactNotFoundException;

    Contact createContact(
            String firstName, String middleName, String lastName, String displayName,
            Collection<ContactData> data
    );

    Contact modifyContact(
            ContactId contactId, long version,
            String firstName, String middleName, String lastName, String displayName,
            Collection<ContactData> data
    ) throws ContactNotFoundException, EntityConflictException;
}
