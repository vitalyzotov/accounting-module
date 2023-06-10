package ru.vzotov.accounting.interfaces.contacts.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.contacts.ContactsApi;
import ru.vzotov.person.domain.model.ContactData;

public class ContactDataDTOAssembler {
    public static ContactsApi.ContactData toDTO(ContactData data) {
        return data == null ? null : new ContactsApi.ContactData(
                data.mimeType(), data.value(), data.type()
        );
    }
}
