package ru.vzotov.cashreceipt.domain.model;

import java.time.LocalDate;
import java.util.List;

public interface ReceiptRepository {
    Receipt find(ReceiptId id);

    Receipt findByQRCodeData(QRCodeData data);

    void store(Receipt receipt);

    List<Receipt> findByDate(LocalDate fromDate, LocalDate toDate);
    long countByDate(LocalDate fromDate, LocalDate toDate);

    Receipt findOldest();
}
