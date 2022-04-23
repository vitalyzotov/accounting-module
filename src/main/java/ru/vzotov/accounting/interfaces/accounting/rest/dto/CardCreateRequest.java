package ru.vzotov.accounting.interfaces.accounting.rest.dto;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.AccountBindingDTO;

import java.time.YearMonth;
import java.util.List;

public class CardCreateRequest {
    private String cardNumber;
    private YearMonth validThru;
    private String issuer;
    private String owner;
    private List<AccountBindingDTO> accounts;

    public CardCreateRequest() {
    }

    public CardCreateRequest(String cardNumber, YearMonth validThru, String issuer) {
        this.cardNumber = cardNumber;
        this.validThru = validThru;
        this.issuer = issuer;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<AccountBindingDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountBindingDTO> accounts) {
        this.accounts = accounts;
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
