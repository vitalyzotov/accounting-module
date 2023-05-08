package ru.vzotov.accounting.interfaces.contacts.facade;

import ru.vzotov.accounting.interfaces.common.EntityConflictException;
import ru.vzotov.accounting.interfaces.contacts.facade.dto.ContactDTO;
import ru.vzotov.accounting.interfaces.contacts.facade.dto.ContactDataDTO;
import ru.vzotov.accounting.interfaces.contacts.facade.dto.ContactNotFoundException;
import ru.vzotov.person.domain.model.ContactId;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ContactsFacade {

    List<ContactDTO> listContacts();

    ContactDTO getContact(ContactId contactId) throws ContactNotFoundException;

    ContactDTO deleteContact(ContactId contactId) throws ContactNotFoundException;

    ContactDTO createContact(
            String firstName, String middleName, String lastName, String displayName,
            Collection<ContactDataDTO> data
    );

    ContactDTO modifyContact(
            ContactId contactId, long version,
            String firstName, String middleName, String lastName, String displayName,
            Collection<ContactDataDTO> data
    ) throws ContactNotFoundException, EntityConflictException;
}
