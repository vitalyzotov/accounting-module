package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;
import java.util.Objects;

public class CardOperationDTO extends OperationRef {
    private String cardNumber;
    private PosTerminalDTO terminal;
    private LocalDate authDate;
    private LocalDate purchaseDate;
    private MoneyDTO amount;
    private String extraInfo;
    private String mcc;

    public CardOperationDTO() {
    }

    public CardOperationDTO(String operationId, String cardNumber, PosTerminalDTO terminal, LocalDate authDate, LocalDate purchaseDate, MoneyDTO amount, String extraInfo, String mcc) {
        super(operationId);
        this.cardNumber = cardNumber;
        this.terminal = terminal;
        this.authDate = authDate;
        this.purchaseDate = purchaseDate;
        this.amount = amount;
        this.extraInfo = extraInfo;
        this.mcc = mcc;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public PosTerminalDTO getTerminal() {
        return terminal;
    }

    public void setTerminal(PosTerminalDTO terminal) {
        this.terminal = terminal;
    }

    public LocalDate getAuthDate() {
        return authDate;
    }

    public void setAuthDate(LocalDate authDate) {
        this.authDate = authDate;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public MoneyDTO getAmount() {
        return amount;
    }

    public void setAmount(MoneyDTO amount) {
        this.amount = amount;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CardOperationDTO that = (CardOperationDTO) o;

        if (!Objects.equals(cardNumber, that.cardNumber)) return false;
        if (!Objects.equals(terminal, that.terminal)) return false;
        if (!Objects.equals(authDate, that.authDate)) return false;
        if (!Objects.equals(purchaseDate, that.purchaseDate)) return false;
        if (!Objects.equals(amount, that.amount)) return false;
        if (!Objects.equals(extraInfo, that.extraInfo)) return false;
        return Objects.equals(mcc, that.mcc);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (cardNumber != null ? cardNumber.hashCode() : 0);
        result = 31 * result + (terminal != null ? terminal.hashCode() : 0);
        result = 31 * result + (authDate != null ? authDate.hashCode() : 0);
        result = 31 * result + (purchaseDate != null ? purchaseDate.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (extraInfo != null ? extraInfo.hashCode() : 0);
        result = 31 * result + (mcc != null ? mcc.hashCode() : 0);
        return result;
    }
}
