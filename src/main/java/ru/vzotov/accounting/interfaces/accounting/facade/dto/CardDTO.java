package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public class CardDTO {
    private String cardNumber;
    private LocalDate validThru;
    private String issuer;
    private String owner;

    public CardDTO() {
    }

    public CardDTO(String cardNumber, String owner, LocalDate validThru, String issuer) {
        this.cardNumber = cardNumber;
        this.owner = owner;
        this.validThru = validThru;
        this.issuer = issuer;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getValidThru() {
        return validThru;
    }

    public void setValidThru(LocalDate validThru) {
        this.validThru = validThru;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}

