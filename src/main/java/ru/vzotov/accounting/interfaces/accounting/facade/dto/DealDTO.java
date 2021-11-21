package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;
import java.util.List;

public class DealDTO {

    private String dealId;

    private LocalDate date;

    private MoneyDTO amount;

    private String description;

    private String comment;

    private Long category;

    private List<String> receipts;

    private List<String> operations;

    public DealDTO() {
    }

    public DealDTO(String dealId, LocalDate date, MoneyDTO amount, String description, String comment,
                   Long category, List<String> receipts, List<String> operations) {
        this.dealId = dealId;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.comment = comment;
        this.category = category;
        this.receipts = receipts;
        this.operations = operations;
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

    public List<String> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<String> receipts) {
        this.receipts = receipts;
    }

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }
}
