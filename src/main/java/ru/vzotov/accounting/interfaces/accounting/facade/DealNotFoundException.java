package ru.vzotov.accounting.interfaces.accounting.facade;

public class DealNotFoundException extends Exception {

    public DealNotFoundException() {
    }

    public DealNotFoundException(String message) {
        super(message);
    }

    public DealNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
