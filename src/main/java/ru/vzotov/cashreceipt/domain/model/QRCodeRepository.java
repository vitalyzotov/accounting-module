package ru.vzotov.cashreceipt.domain.model;

import java.time.LocalDate;
import java.util.List;

public interface QRCodeRepository {
    QRCode find(ReceiptId id);

    QRCode findByQRCodeData(QRCodeData data);

    void store(QRCode qrCode);

    List<QRCode> findByDate(LocalDate fromDate, LocalDate toDate);

    List<QRCode> findAllInState(ReceiptState state);

    List<QRCode> findWithoutDeals();
}
