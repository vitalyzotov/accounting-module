package ru.vzotov.accounting.interfaces.accounting.facade.dto;

public class BudgetNotFoundException extends Exception {
    public BudgetNotFoundException() {
    }

    public BudgetNotFoundException(String message) {
        super(message);
    }

    public BudgetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
