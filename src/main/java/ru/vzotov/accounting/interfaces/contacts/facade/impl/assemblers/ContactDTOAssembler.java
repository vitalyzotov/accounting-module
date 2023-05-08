package ru.vzotov.accounting.interfaces.contacts.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.contacts.facade.dto.ContactDTO;
import ru.vzotov.person.domain.model.Contact;
import ru.vzotov.person.domain.model.ContactData;

import java.util.Comparator;
import java.util.stream.Collectors;

public class ContactDTOAssembler {

    public static ContactDTO toDTO(Contact contact) {
        return contact == null ? null : new ContactDTO(
                contact.contactId().value(),
                contact.version(),
                contact.owner().value(),
                contact.firstName(),
                contact.middleName(),
                contact.lastName(),
                contact.displayName(),
                contact.data() == null ? null :
                        contact.data().stream()
                                .sorted(Comparator.comparing(ContactData::mimeType))
                                .map(ContactDataDTOAssembler::toDTO)
                                .collect(Collectors.toList()),
                contact.created(),
                contact.updated()
        );
    }
}
