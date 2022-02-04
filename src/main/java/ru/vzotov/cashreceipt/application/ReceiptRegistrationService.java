package ru.vzotov.cashreceipt.application;

import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;

import java.io.IOException;

public interface ReceiptRegistrationService {

    /**
     * Register receipt
     *
     * @param qrCodeData QR data of receipt
     * @return receipt ID
     */
    ReceiptId register(QRCodeData qrCodeData) throws ReceiptNotFoundException, IOException;

    /**
     * Performs loading of receipt details from external systems
     *
     * @param qrCodeData QR data of the receipt
     * @return receipt ID
     * @throws ReceiptNotFoundException if receipt was not found
     * @throws IOException            in case of I/O errors when loading receipt details
     */
    ReceiptId loadDetails(QRCodeData qrCodeData) throws ReceiptNotFoundException, IOException;

    void loadNewReceipts();

}
