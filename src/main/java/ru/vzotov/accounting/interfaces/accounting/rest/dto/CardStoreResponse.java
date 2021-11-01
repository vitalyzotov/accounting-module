package ru.vzotov.accounting.interfaces.accounting.rest.dto;

public class CardStoreResponse {
    private String cardNumber;

    public CardStoreResponse() {

    }

    public CardStoreResponse(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
