package ru.vzotov.accounting.interfaces.contacts.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.contacts.facade.dto.ContactDataDTO;
import ru.vzotov.person.domain.model.ContactData;

public class ContactDataDTOAssembler {
    public static ContactDataDTO toDTO(ContactData data) {
        return data == null ? null : new ContactDataDTO(
                data.mimeType(), data.value(), data.type()
        );
    }
}
