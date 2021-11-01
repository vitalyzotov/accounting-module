package ru.vzotov.accounting.interfaces.accounting.facade.dto;

public class TransactionDTO {

    private String primary;

    private String secondary;

    public TransactionDTO() {
    }

    public TransactionDTO(String primary, String secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }
}
