package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public class CardDTO {
    private String cardNumber;
    private LocalDate validThru;
    private String issuer;

    public CardDTO() {
    }

    public CardDTO(String cardNumber, LocalDate validThru, String issuer) {
        this.cardNumber = cardNumber;
        this.validThru = validThru;
        this.issuer = issuer;
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

