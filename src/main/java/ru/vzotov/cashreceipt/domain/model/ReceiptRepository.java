package ru.vzotov.cashreceipt.domain.model;

import ru.vzotov.person.domain.model.PersonId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface ReceiptRepository {
    Receipt find(ReceiptId id);

    Receipt findByQRCodeData(QRCodeData data);

    void store(Receipt receipt);

    List<Receipt> findByDate(Collection<PersonId> owners, LocalDate fromDate, LocalDate toDate);
    long countByDate(Collection<PersonId> owners, LocalDate fromDate, LocalDate toDate);

    Receipt findOldest(Collection<PersonId> owners);

    boolean delete(ReceiptId id);
}
