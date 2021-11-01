package ru.vzotov.accounting.interfaces.accounting.facade.dto;

public class BudgetPlanNotFoundException extends Exception {

    public BudgetPlanNotFoundException() {
    }

    public BudgetPlanNotFoundException(String message) {
        super(message);
    }

    public BudgetPlanNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
