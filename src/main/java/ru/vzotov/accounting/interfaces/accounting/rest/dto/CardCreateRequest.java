package ru.vzotov.accounting.interfaces.accounting.rest.dto;

import java.time.YearMonth;

public class CardCreateRequest {
    private String cardNumber;
    private YearMonth validThru;
    private String issuer;

    public CardCreateRequest() {
    }

    public CardCreateRequest(String cardNumber, YearMonth validThru, String issuer) {
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

    public YearMonth getValidThru() {
        return validThru;
    }

    public void setValidThru(YearMonth validThru) {
        this.validThru = validThru;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
