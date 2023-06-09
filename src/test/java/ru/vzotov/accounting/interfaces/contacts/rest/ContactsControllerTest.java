package ru.vzotov.accounting.interfaces.contacts.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import ru.vzotov.accounting.interfaces.contacts.ContactsApi;
import ru.vzotov.accounting.test.AbstractControllerTest;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class ContactsControllerTest extends AbstractControllerTest {

    private static final Logger log = LoggerFactory.getLogger(ContactsControllerTest.class);

    @Test
    public void testGetContact() {
        final String contactId = "3f44dd23-3d7c-42ba-b5c3-4526cf4c098d";
        final ResponseEntity<ContactsApi.Contact> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts/{contactId}",
                HttpMethod.GET, new HttpEntity<>(null),
                ContactsApi.Contact.class,
                contactId
        );

        assertThat(exchange.getBody()).isNotNull();
        assertThat(exchange.getBody().lastName()).isEqualTo("Ivanov");
        assertThat(exchange.getBody().version()).isEqualTo(1);
    }

    @Test
    public void testDeleteContact() {
        final String contactId = "9ba67847-fdfe-431e-be58-6c892f17d270";
        final ResponseEntity<ContactsApi.Contact> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts/{contactId}",
                HttpMethod.DELETE, new HttpEntity<>(null),
                ContactsApi.Contact.class,
                contactId
        );

        assertThat(exchange.getBody()).isNotNull();
        assertThat(exchange.getBody().id()).isEqualTo(contactId);

        final ResponseEntity<ContactsApi.Contact> exchange2 = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts/{contactId}",
                HttpMethod.GET, new HttpEntity<>(null),
                ContactsApi.Contact.class,
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
        final ResponseEntity<ContactsApi.Contact[]> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts",
                HttpMethod.GET, new HttpEntity<>(null),
                ContactsApi.Contact[].class
        );

        assertThat(exchange.getBody()).isNotNull();
        assertThat(exchange.getBody().length).isGreaterThan(0);
        assertThat(exchange.getBody()).allMatch(dto -> PERSON_ID.equals(dto.owner()));
    }

    @Test
    public void testCreateContact() {
        final ContactsApi.Contact.Create request = new ContactsApi.Contact.Create(
                "Custom", null, "Lastname", null,
                Collections.singletonList(new ContactsApi.ContactData("mimetype1", "value1", null))
        );
        final ResponseEntity<ContactsApi.Contact> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts",
                HttpMethod.POST,
                new HttpEntity<>(request),
                ContactsApi.Contact.class
        );

        final ContactsApi.Contact body = exchange.getBody();

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.owner()).isEqualTo(PERSON_ID);
        assertThat(body.firstName()).isEqualTo(request.firstName());
        assertThat(body.middleName()).isEqualTo(request.middleName());
        assertThat(body.lastName()).isEqualTo(request.lastName());
        assertThat(body.data().size()).isEqualTo(request.data().size());

        final ResponseEntity<ContactsApi.Contact> exchange2 = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts/{contactId}",
                HttpMethod.GET, new HttpEntity<>(null),
                ContactsApi.Contact.class,
                body.id()
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
        final ResponseEntity<ContactsApi.Contact> getContact = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts/{contactId}",
                HttpMethod.GET, new HttpEntity<>(null),
                ContactsApi.Contact.class,
                contactId
        );
        log.info("getContact: {}", getContact.toString());
        log.info("getContact response: {}", toJSON(getContact.getBody()));

        assertThat(getContact.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getContact.getBody()).isNotNull();

        final ContactsApi.Contact dto = getContact.getBody();

        // Try to perform modification with conflict
        final ContactsApi.Contact.Modify conflictRequest = new ContactsApi.Contact.Modify(
                1000L, "Modified", null, "Contact", null,
                Collections.singletonList(new ContactsApi.ContactData("mimetype1", "value1", null))
        );
        final ResponseEntity<ContactsApi.Contact> modifyContactWithConflict = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts/{contactId}",
                HttpMethod.PUT,
                new HttpEntity<>(conflictRequest),
                ContactsApi.Contact.class,
                contactId
        );

        assertThat(modifyContactWithConflict.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

        // Perform modification
        final ContactsApi.Contact.Modify request = new ContactsApi.Contact.Modify(
                dto.version(), "Modified", null, "Contact", null,
                Collections.singletonList(new ContactsApi.ContactData("mimetype1", "value1", null))
        );
        final ResponseEntity<ContactsApi.Contact> modifyContact = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts/{contactId}",
                HttpMethod.PUT,
                new HttpEntity<>(request),
                ContactsApi.Contact.class,
                contactId
        );

        log.info("modifyContact: {}", modifyContact.toString());
        log.info("modifyContact response: {}", toJSON(modifyContact.getBody()));
        final ContactsApi.Contact modification = modifyContact.getBody();

        assertThat(modifyContact.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(modification).isNotNull();
        assertThat(modification.owner()).isEqualTo(PERSON_ID);
        assertThat(modification.firstName()).isEqualTo(request.firstName());
        assertThat(modification.middleName()).isEqualTo(request.middleName());
        assertThat(modification.lastName()).isEqualTo(request.lastName());
        assertThat(modification.data().size()).isEqualTo(request.data().size());
        assertThat(modification.version()).isEqualTo(2);

        // Check that contact was modified
        final ResponseEntity<ContactsApi.Contact> getModifiedContact = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/contacts/{contactId}",
                HttpMethod.GET, new HttpEntity<>(null),
                ContactsApi.Contact.class,
                contactId
        );
        log.info("getModifiedContact: {}", getModifiedContact.toString());
        log.info("getModifiedContact response: {}", toJSON(getModifiedContact.getBody()));

        assertThat(getModifiedContact.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getModifiedContact.getBody()).isNotNull();
        assertThat(getModifiedContact.getBody()).usingRecursiveComparison()
                .withComparatorForType(
                        Comparator.comparing(a -> a.truncatedTo(ChronoUnit.MILLIS).toInstant()),
                        OffsetDateTime.class
                )
                .isEqualTo(modification);

    }
}
