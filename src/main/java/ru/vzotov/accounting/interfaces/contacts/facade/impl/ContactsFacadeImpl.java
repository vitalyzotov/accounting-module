package ru.vzotov.accounting.interfaces.contacts.facade.impl;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.domain.model.ContactRepository;
import ru.vzotov.accounting.infrastructure.security.SecurityUtils;
import ru.vzotov.accounting.interfaces.common.EntityConflictException;
import ru.vzotov.accounting.interfaces.contacts.ContactsApi;
import ru.vzotov.accounting.interfaces.contacts.facade.ContactsFacade;
import ru.vzotov.accounting.interfaces.contacts.facade.ContactNotFoundException;
import ru.vzotov.accounting.interfaces.contacts.facade.impl.assemblers.ContactDTOAssembler;
import ru.vzotov.person.domain.model.Contact;
import ru.vzotov.person.domain.model.ContactData;
import ru.vzotov.person.domain.model.ContactId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ContactsFacadeImpl implements ContactsFacade {

    private final ContactRepository contactRepository;

    public ContactsFacadeImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<ContactsApi.Contact> listContacts() {
        return contactRepository.find(SecurityUtils.getCurrentPerson()).stream()
                .map(ContactDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ContactsApi.Contact getContact(ContactId contactId) throws ContactNotFoundException {
        return Optional.ofNullable(ContactDTOAssembler.toDTO(findSecurely(contactId)))
                .orElseThrow(ContactNotFoundException::new);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ContactsApi.Contact deleteContact(ContactId contactId) throws ContactNotFoundException {
        final Contact contact = findSecurely(contactId);
        if (contact == null) throw new ContactNotFoundException();
        return contactRepository.delete(contactId) ? ContactDTOAssembler.toDTO(contact) : null;
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ContactsApi.Contact createContact(String firstName, String middleName, String lastName, String displayName, Collection<ContactsApi.ContactData> data) {
        final Contact contact = new Contact(
                ContactId.nextId(), SecurityUtils.getCurrentPerson(),
                firstName, middleName, lastName, displayName, toModel(data)
        );
        contactRepository.store(contact);
        return ContactDTOAssembler.toDTO(contact);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ContactsApi.Contact modifyContact(ContactId contactId, long version,
                                             String firstName, String middleName, String lastName, String displayName,
                                             Collection<ContactsApi.ContactData> data)
            throws ContactNotFoundException, EntityConflictException {
        final Contact contact = findSecurely(contactId);
        if (contact == null) throw new ContactNotFoundException();
        if (contact.version() != version) throw new EntityConflictException();
        contact.update(firstName, middleName, lastName, displayName, toModel(data));
        contactRepository.store(contact);
        return ContactDTOAssembler.toDTO(contact);

    }

    @PostAuthorize("hasAuthority(returnObject.owner)")
    private Contact findSecurely(ContactId contactId) {
        return contactRepository.find(contactId);
    }

    private Set<ContactData> toModel(Collection<ContactsApi.ContactData> data) {
        return data == null ? null :
                data.stream()
                        .map(dto -> new ContactData(dto.mimeType(), dto.value(), dto.type()))
                        .collect(Collectors.toSet());
    }
}
