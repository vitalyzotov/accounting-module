package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseRef;

import java.time.LocalDate;
import java.util.List;

public record DealDTO(String dealId, String owner, LocalDate date, MoneyDTO amount, String description, String comment,
                      Long category, List<ReceiptRef> receipts, List<OperationRef> operations,
                      List<OperationRef> cardOperations, List<PurchaseRef> purchases) {

}
