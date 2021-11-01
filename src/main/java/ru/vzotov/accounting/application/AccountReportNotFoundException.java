package ru.vzotov.accounting.application;

public class AccountReportNotFoundException extends Exception {

    public AccountReportNotFoundException() {
    }

    public AccountReportNotFoundException(String message) {
        super(message);
    }
}
