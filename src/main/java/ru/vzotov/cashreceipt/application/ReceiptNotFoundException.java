package ru.vzotov.cashreceipt.application;

public class ReceiptNotFoundException extends Exception {

    @SuppressWarnings("WeakerAccess")
    public ReceiptNotFoundException() {
    }

    public ReceiptNotFoundException(String message) {
        super(message);
    }

    public ReceiptNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
