package ru.vzotov.accounting.interfaces.accounting.rest.dto;

public class BankCreateRequest {
    private String bankId;
    private String name;
    private String shortName;
    private String longName;

    public BankCreateRequest() {
    }

    public BankCreateRequest(String bankId, String name, String shortName, String longName) {
        this.bankId = bankId;
        this.name = name;
        this.shortName = shortName;
        this.longName = longName;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }
}
