package ru.vzotov.accounting.interfaces.contacts;

import java.time.OffsetDateTime;
import java.util.List;

public interface ContactsApi {
    record ContactData(String mimeType, String value, String type) {
    }

    record Contact(String id, long version, String owner, String firstName, String middleName, String lastName,
                   String displayName, List<ContactData> data, OffsetDateTime created,
                   OffsetDateTime updated) {
        public record Create(String firstName, String middleName, String lastName, String displayName,
                             List<ContactData> data) {
        }

        public record Modify(long version,
                             String firstName, String middleName, String lastName, String displayName,
                             List<ContactData> data) {
        }
    }

}
