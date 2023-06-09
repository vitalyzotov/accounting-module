package ru.vzotov.accounting.interfaces.accounting.facade;

public class OperationNotFoundException extends Exception {
    public OperationNotFoundException() {
    }

    public OperationNotFoundException(String message) {
        super(message);
    }

    public OperationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
