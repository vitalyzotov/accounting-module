package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseRef;

import java.time.LocalDate;
import java.util.List;

public class DealDTO {

    private String dealId;

    private LocalDate date;

    private MoneyDTO amount;

    private String description;

    private String comment;

    private Long category;

    private List<ReceiptRef> receipts;

    private List<OperationRef> operations;

    private List<OperationRef> cardOperations;

    private List<PurchaseRef> purchases;

    public DealDTO() {
    }

    public DealDTO(String dealId, LocalDate date, MoneyDTO amount, String description, String comment,
                   Long category,
                   List<ReceiptRef> receipts,
                   List<OperationRef> operations,
                   List<OperationRef> cardOperations,
                   List<PurchaseRef> purchases) {
        this.dealId = dealId;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.comment = comment;
        this.category = category;
        this.receipts = receipts;
        this.operations = operations;
        this.cardOperations = cardOperations;
        this.purchases = purchases;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public MoneyDTO getAmount() {
        return amount;
    }

    public void setAmount(MoneyDTO amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public List<ReceiptRef> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<ReceiptRef> receipts) {
        this.receipts = receipts;
    }

    public List<OperationRef> getCardOperations() {
        return cardOperations;
    }

    public void setCardOperations(List<OperationRef> cardOperations) {
        this.cardOperations = cardOperations;
    }

    public List<OperationRef> getOperations() {
        return operations;
    }

    public void setOperations(List<OperationRef> operations) {
        this.operations = operations;
    }

    public List<PurchaseRef> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseRef> purchases) {
        this.purchases = purchases;
    }
}
