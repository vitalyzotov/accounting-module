package ru.vzotov.accounting.interfaces.contacts.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.accounting.interfaces.common.EntityConflictException;
import ru.vzotov.accounting.interfaces.contacts.ContactsApi.Contact;
import ru.vzotov.accounting.interfaces.contacts.facade.ContactNotFoundException;
import ru.vzotov.accounting.interfaces.contacts.facade.ContactsFacade;
import ru.vzotov.person.domain.model.ContactId;

import java.util.List;

@RestController
@RequestMapping("/accounting/contacts")
@CrossOrigin
public class ContactsController {

    private final ContactsFacade contactsFacade;

    public ContactsController(ContactsFacade contactsFacade) {
        this.contactsFacade = contactsFacade;
    }

    @GetMapping("{contactId}")
    public Contact getContact(@PathVariable String contactId) throws ContactNotFoundException {
        return contactsFacade.getContact(new ContactId(contactId));
    }

    @DeleteMapping("{contactId}")
    public Contact deleteContact(@PathVariable String contactId) throws ContactNotFoundException {
        return contactsFacade.deleteContact(new ContactId(contactId));
    }

    @GetMapping
    public List<Contact> listContacts() {
        return contactsFacade.listContacts();
    }

    @PostMapping
    public Contact createContact(@RequestBody Contact.Create request) {
        return contactsFacade.createContact(
                request.firstName(), request.middleName(), request.lastName(), request.displayName(),
                request.data());
    }

    @PutMapping("{contactId}")
    public Contact modifyContact(@PathVariable String contactId, @RequestBody Contact.Modify request)
            throws ContactNotFoundException, EntityConflictException {
        return contactsFacade.modifyContact(
                new ContactId(contactId), request.version(),
                request.firstName(), request.middleName(), request.lastName(), request.displayName(),
                request.data()
        );
    }

}
