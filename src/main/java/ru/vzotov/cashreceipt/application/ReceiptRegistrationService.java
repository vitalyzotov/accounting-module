package ru.vzotov.cashreceipt.application;

import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;

import java.io.IOException;

public interface ReceiptRegistrationService {

    /**
     * Регистрация чека в системе
     *
     * @param qrCodeData сведения QR-кода
     * @return идентификатор QR кода
     */
    ReceiptId register(QRCodeData qrCodeData) throws ReceiptNotFoundException, IOException;

    /**
     * Инициирует сбор информации о чеке из внешних систем, например, из налоговой службы.
     *
     * @param qrCodeData сведения из QR-кода
     * @return идентификатор чека
     * @throws ReceiptNotFoundException если чек не был найден
     * @throws IOException            при ошибках ввода-вывода
     */
    ReceiptId loadDetails(QRCodeData qrCodeData) throws ReceiptNotFoundException, IOException;

    void loadNewReceipts();

}
