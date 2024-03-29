package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.ContactRepository;
import ru.vzotov.person.domain.model.Contact;
import ru.vzotov.person.domain.model.ContactData;
import ru.vzotov.person.domain.model.ContactId;
import ru.vzotov.person.domain.model.PersonId;

import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class ContactRepositoryJpaTest {
    private static final Logger log = LoggerFactory.getLogger(BudgetRepositoryJpaTest.class);
    private static final PersonId PERSON_ID = new PersonId("c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2");

    @Autowired
    private ContactRepository contactRepository;

    @Test
    public void testStore() {

        Set<ContactData> data = new HashSet<>();
        data.add(new ContactData("mime1", "value1", null));

        Contact contact = new Contact(ContactId.nextId(), PERSON_ID, "First", "Middle", "Last", "Display", data);
        contactRepository.store(contact);
        assertThat(contact.version()).isEqualTo(0);

    }

    @Test
    public void testFind() {
        Contact contact = contactRepository.find(new ContactId("3f44dd23-3d7c-42ba-b5c3-4526cf4c098d"));
        assertThat(contact).isNotNull();
        assertThat(contact.data())
                .hasSize(2)
                .are(new Condition<>() {
                    @Override
                    public boolean matches(ContactData value) {
                        return value.mimeType() != null || value.value() != null;
                    }
                })
        ;
    }

    @Test
    public void testFindByOwner() {
        final PersonId owner = new PersonId("08d834ae-5bc3-439c-81ee-ad8169e8588b");
        final List<Contact> contacts = contactRepository.find(owner);
        assertThat(contacts).hasSize(1).are(new Condition<>() {
            @Override
            public boolean matches(Contact value) {
                return value.owner().equals(owner);
            }
        });
    }

    @Test
    public void testDelete() {
        final ContactId contactId = new ContactId("21649b67-dd43-48f2-961a-7343aa3c31a4");
        assertThat(contactRepository.delete(contactId)).isTrue();
        assertThat(contactRepository.find(contactId)).isNull();
    }

}
