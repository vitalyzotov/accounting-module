package ru.vzotov.accounting.interfaces.common;

public class EntityConflictException extends Exception {

    public EntityConflictException() {
    }

    public EntityConflictException(String message) {
        super(message);
    }

    public EntityConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
