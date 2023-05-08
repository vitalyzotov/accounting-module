package ru.vzotov.accounting.interfaces.contacts.facade.dto;

public class ContactNotFoundException extends Exception {

    public ContactNotFoundException() {
    }

    public ContactNotFoundException(String message) {
        super(message);
    }

    public ContactNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
