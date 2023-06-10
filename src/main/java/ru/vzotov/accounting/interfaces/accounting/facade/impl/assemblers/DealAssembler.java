package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.domain.model.Deal;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.purchases.PurchasesApi;
import ru.vzotov.accounting.interfaces.purchases.facade.impl.assembler.PurchaseAssembler;

import java.util.List;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static ru.vzotov.accounting.interfaces.common.assembler.MoneyAssembler.money;

public class DealAssembler {

    private static final QRCodeAssembler receiptAssembler = new QRCodeAssembler();
    private static final PurchaseAssembler purchaseAssembler = new PurchaseAssembler();

    public static AccountingApi.Deal toDTO(Deal deal) {
        return toDTO(deal, null, null, null, null);
    }

    public static AccountingApi.Deal toDTO(Deal deal,
                                           Function<Deal, List<AccountingApi.ReceiptRef>> receipts,
                                           Function<Deal, List<AccountingApi.OperationRef>> operations,
                                           Function<Deal, List<AccountingApi.OperationRef>> cardOperations,
                                           Function<Deal, List<PurchasesApi.PurchaseRef>> purchases) {
        return deal == null ? null : new AccountingApi.Deal(
                deal.dealId().value(),
                deal.owner().value(),
                deal.date(),
                money(deal.amount()),
                deal.description(),
                deal.comment(),
                deal.category() == null ? null : deal.category().id(),
                ofNullable(receipts).orElse(DealAssembler::receipts).apply(deal),
                ofNullable(operations).orElse(DealAssembler::operations).apply(deal),
                ofNullable(cardOperations).orElse(DealAssembler::cardOperations).apply(deal),
                ofNullable(purchases).orElse(DealAssembler::purchases).apply(deal)
        );
    }

    public static List<AccountingApi.ReceiptRef> receipts(Deal deal) {
        return deal.receipts().stream().map(receiptAssembler::toRef).toList();
    }

    public static List<AccountingApi.OperationRef> operations(Deal deal) {
        return deal.operations().stream().map(OperationAssembler::toRef).toList();
    }

    public static List<AccountingApi.OperationRef> cardOperations(Deal deal) {
        return deal.cardOperations().stream().map(OperationAssembler::toRef).toList();
    }

    public static List<PurchasesApi.PurchaseRef> purchases(Deal deal) {
        return deal.purchases().stream().map(purchaseAssembler::toRef).toList();
    }
}
