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
import ru.vzotov.accounting.interfaces.contacts.facade.ContactsFacade;
import ru.vzotov.accounting.interfaces.contacts.facade.dto.ContactDTO;
import ru.vzotov.accounting.interfaces.contacts.facade.dto.ContactNotFoundException;
import ru.vzotov.accounting.interfaces.contacts.rest.dto.ContactCreateRequest;
import ru.vzotov.accounting.interfaces.contacts.rest.dto.ContactModifyRequest;
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
    public ContactDTO getContact(@PathVariable String contactId) throws ContactNotFoundException {
        return contactsFacade.getContact(new ContactId(contactId));
    }

    @DeleteMapping("{contactId}")
    public ContactDTO deleteContact(@PathVariable String contactId)
            throws ContactNotFoundException {
        return contactsFacade.deleteContact(new ContactId(contactId));
    }

    @GetMapping
    public List<ContactDTO> listContacts() {
        return contactsFacade.listContacts();
    }

    @PostMapping
    public ContactDTO createContact(@RequestBody ContactCreateRequest request) {
        return contactsFacade.createContact(
                request.getFirstName(), request.getMiddleName(), request.getLastName(), request.getDisplayName(),
                request.getData());
    }

    @PutMapping("{contactId}")
    public ContactDTO modifyContact(@PathVariable String contactId, @RequestBody ContactModifyRequest request)
            throws ContactNotFoundException, EntityConflictException {
        return contactsFacade.modifyContact(
                new ContactId(contactId), request.getVersion(),
                request.getFirstName(), request.getMiddleName(), request.getLastName(), request.getDisplayName(),
                request.getData()
        );
    }

}
