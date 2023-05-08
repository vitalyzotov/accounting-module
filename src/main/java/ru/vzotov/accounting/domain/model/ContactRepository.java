package ru.vzotov.accounting.domain.model;

import ru.vzotov.person.domain.model.Contact;
import ru.vzotov.person.domain.model.ContactId;
import ru.vzotov.person.domain.model.PersonId;

import java.util.List;

public interface ContactRepository {

    Contact find(ContactId contactId);

    List<Contact> find(PersonId owner);

    void store(Contact contact);

    boolean delete(ContactId contactId);

}
