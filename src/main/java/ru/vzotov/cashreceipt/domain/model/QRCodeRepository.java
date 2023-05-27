package ru.vzotov.cashreceipt.domain.model;

import ru.vzotov.person.domain.model.PersonId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface QRCodeRepository {
    QRCode find(ReceiptId id);

    QRCode findByQRCodeData(QRCodeData data);

    void store(QRCode qrCode);

    List<QRCode> findByDate(Collection<PersonId> owners, LocalDate fromDate, LocalDate toDate);

    List<QRCode> findAllInState(ReceiptState state);

    List<QRCode> findWithoutDeals();

    boolean delete(ReceiptId id);
}
