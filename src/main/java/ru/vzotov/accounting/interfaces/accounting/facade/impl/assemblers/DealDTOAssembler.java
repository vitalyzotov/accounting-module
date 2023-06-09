package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.domain.model.Deal;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseRef;
import ru.vzotov.accounting.interfaces.purchases.facade.impl.assembler.PurchaseDTOAssembler;

import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

public class DealDTOAssembler {

    private static final QRCodeDTOAssembler receiptAssembler = new QRCodeDTOAssembler();
    private static final PurchaseDTOAssembler purchaseAssembler = new PurchaseDTOAssembler();

    public static AccountingApi.Deal toDTO(Deal deal) {
        return toDTO(deal, null, null, null, null);
    }

    public static AccountingApi.Deal toDTO(Deal deal,
                                           Function<Deal, List<AccountingApi.ReceiptRef>> receipts,
                                           Function<Deal, List<AccountingApi.OperationRef>> operations,
                                           Function<Deal, List<AccountingApi.OperationRef>> cardOperations,
                                           Function<Deal, List<PurchaseRef>> purchases) {
        return deal == null ? null : new AccountingApi.Deal(
                deal.dealId().value(),
                deal.owner().value(),
                deal.date(),
                MoneyDTOAssembler.toDTO(deal.amount()),
                deal.description(),
                deal.comment(),
                deal.category() == null ? null : deal.category().id(),
                ofNullable(receipts).orElse(DealDTOAssembler::receipts).apply(deal),
                ofNullable(operations).orElse(DealDTOAssembler::operations).apply(deal),
                ofNullable(cardOperations).orElse(DealDTOAssembler::cardOperations).apply(deal),
                ofNullable(purchases).orElse(DealDTOAssembler::purchases).apply(deal)
        );
    }

    public static List<AccountingApi.ReceiptRef> receipts(Deal deal) {
        return deal.receipts().stream().map(receiptAssembler::toRef).toList();
    }

    public static List<AccountingApi.OperationRef> operations(Deal deal) {
        return deal.operations().stream().map(OperationDTOAssembler::toRef).toList();
    }

    public static List<AccountingApi.OperationRef> cardOperations(Deal deal) {
        return deal.cardOperations().stream().map(OperationDTOAssembler::toRef).toList();
    }

    public static List<PurchaseRef> purchases(Deal deal) {
        return deal.purchases().stream().map(purchaseAssembler::toRef).toList();
    }
}
