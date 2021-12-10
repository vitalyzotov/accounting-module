package ru.vzotov.cashreceipt.application;

public class ReceiptItemNotFoundException extends Exception {

    @SuppressWarnings("WeakerAccess")
    public ReceiptItemNotFoundException() {
    }

    public ReceiptItemNotFoundException(String message) {
        super(message);
    }

    public ReceiptItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
