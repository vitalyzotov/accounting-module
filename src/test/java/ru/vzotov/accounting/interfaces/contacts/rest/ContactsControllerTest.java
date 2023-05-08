package ru.vzotov.accounting.interfaces.contacts.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.accounting.interfaces.contacts.facade.dto.ContactDTO;
import ru.vzotov.accounting.interfaces.contacts.facade.dto.ContactDataDTO;
import ru.vzotov.accounting.interfaces.contacts.rest.dto.ContactCreateRequest;
import ru.vzotov.accounting.interfaces.contacts.rest.dto.ContactModifyRequest;
import ru.vzotov.accounting.test.AbstractControllerTest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class ContactsControllerTest extends AbstractControllerTest {

    private static final Logger log = LoggerFactory.getLogger(ContactsControllerTest.class);

    @Test
    public void testGetContact() {
        final String contactId = "3f44dd23-3d7c-42ba-b5c3-4526cf4c098d";
        final ResponseEntity<ContactDTO> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts/{contactId}",
                HttpMethod.GET, new HttpEntity<>(null),
                ContactDTO.class,
                contactId
        );

        assertThat(exchange.getBody()).isNotNull();
        assertThat(exchange.getBody().getLastName()).isEqualTo("Ivanov");
        assertThat(exchange.getBody().getVersion()).isEqualTo(1);
    }

    @Test
    public void testDeleteContact() {
        final String contactId = "9ba67847-fdfe-431e-be58-6c892f17d270";
        final ResponseEntity<ContactDTO> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts/{contactId}",
                HttpMethod.DELETE, new HttpEntity<>(null),
                ContactDTO.class,
                contactId
        );

        assertThat(exchange.getBody()).isNotNull();
        assertThat(exchange.getBody().getId()).isEqualTo(contactId);

        final ResponseEntity<ContactDTO> exchange2 = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts/{contactId}",
                HttpMethod.GET, new HttpEntity<>(null),
                ContactDTO.class,
                contactId
        );

        log.info(exchange2.toString());
        log.info(toJSON(exchange2.getBody()));
        assertThat(
                exchange2.getStatusCode()
        ).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testListContacts() {
        final ResponseEntity<ContactDTO[]> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts",
                HttpMethod.GET, new HttpEntity<>(null),
                ContactDTO[].class
        );

        assertThat(exchange.getBody()).isNotNull();
        assertThat(exchange.getBody().length).isGreaterThan(0);
        assertThat(exchange.getBody()).allMatch(dto -> PERSON_ID.equals(dto.getOwner()));
    }

    @Test
    public void testCreateContact() {
        final ContactCreateRequest request = new ContactCreateRequest(
                "Custom", null, "Lastname", null,
                Collections.singletonList(new ContactDataDTO("mimetype1", "value1", null))
        );
        final ResponseEntity<ContactDTO> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts",
                HttpMethod.POST,
                new HttpEntity<>(request),
                ContactDTO.class
        );

        final ContactDTO body = exchange.getBody();

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.getOwner()).isEqualTo(PERSON_ID);
        assertThat(body.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(body.getMiddleName()).isEqualTo(request.getMiddleName());
        assertThat(body.getLastName()).isEqualTo(request.getLastName());
        assertThat(body.getData().size()).isEqualTo(request.getData().size());

        final ResponseEntity<ContactDTO> exchange2 = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts/{contactId}",
                HttpMethod.GET, new HttpEntity<>(null),
                ContactDTO.class,
                body.getId()
        );
        log.info(exchange2.toString());
        log.info(toJSON(exchange2.getBody()));

        assertThat(
                exchange2.getStatusCode()
        ).isEqualTo(HttpStatus.OK);

    }

    @Test
    public void testModifyContact() throws JsonProcessingException {
        final String contactId = "4bfabcc5-b93f-40af-abef-3eba1a5cb0ce";

        // Get contact
        final ResponseEntity<ContactDTO> getContact = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts/{contactId}",
                HttpMethod.GET, new HttpEntity<>(null),
                ContactDTO.class,
                contactId
        );
        log.info("getContact: {}", getContact.toString());
        log.info("getContact response: {}", toJSON(getContact.getBody()));

        assertThat(getContact.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getContact.getBody()).isNotNull();

        final ContactDTO dto = getContact.getBody();

        // Try to perform modification with conflict
        final ContactModifyRequest conflictRequest = new ContactModifyRequest(
                1000L, "Modified", null, "Contact", null,
                Collections.singletonList(new ContactDataDTO("mimetype1", "value1", null))
        );
        final ResponseEntity<ContactDTO> modifyContactWithConflict = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts/{contactId}",
                HttpMethod.PUT,
                new HttpEntity<>(conflictRequest),
                ContactDTO.class,
                contactId
        );

        assertThat(modifyContactWithConflict.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        // Perform modification
        final ContactModifyRequest request = new ContactModifyRequest(
                dto.getVersion(), "Modified", null, "Contact", null,
                Collections.singletonList(new ContactDataDTO("mimetype1", "value1", null))
        );
        final ResponseEntity<ContactDTO> modifyContact = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts/{contactId}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                ContactDTO.class,
                contactId
        );

        log.info("modifyContact: {}", modifyContact.toString());
        log.info("modifyContact response: {}", toJSON(modifyContact.getBody()));
        final ContactDTO modification = modifyContact.getBody();

        assertThat(modifyContact.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(modification).isNotNull();
        assertThat(modification.getOwner()).isEqualTo(PERSON_ID);
        assertThat(modification.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(modification.getMiddleName()).isEqualTo(request.getMiddleName());
        assertThat(modification.getLastName()).isEqualTo(request.getLastName());
        assertThat(modification.getData().size()).isEqualTo(request.getData().size());
        assertThat(modification.getVersion()).isEqualTo(2);

        // Check that contact was modified
        final ResponseEntity<ContactDTO> getModifiedContact = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts/{contactId}",
                HttpMethod.GET, new HttpEntity<>(null),
                ContactDTO.class,
                contactId
        );
        log.info("getModifiedContact: {}", getModifiedContact.toString());
        log.info("getModifiedContact response: {}",toJSON(getModifiedContact.getBody()));

        assertThat(getModifiedContact.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getModifiedContact.getBody()).isNotNull();
        assertThat(getModifiedContact.getBody()).usingRecursiveComparison().isEqualTo(modification);

    }
}
