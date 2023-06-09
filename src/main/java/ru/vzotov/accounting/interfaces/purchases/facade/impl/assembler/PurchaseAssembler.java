package ru.vzotov.accounting.interfaces.purchases.facade.impl.assembler;

import ru.vzotov.accounting.interfaces.common.assembler.AbstractAssembler;
import ru.vzotov.accounting.interfaces.purchases.PurchasesApi;
import ru.vzotov.purchase.domain.model.Purchase;
import ru.vzotov.purchase.domain.model.PurchaseId;

import static ru.vzotov.accounting.interfaces.common.assembler.MoneyAssembler.money;

public class PurchaseAssembler extends AbstractAssembler<PurchasesApi.Purchase, Purchase> {

    public PurchasesApi.PurchaseRef toRef(PurchaseId model) {
        return model == null ? null : new PurchasesApi.PurchaseId(model.value());
    }

    public PurchasesApi.PurchaseRef toRef(Purchase model) {
        return model == null ? null : toRef(model.purchaseId());
    }

    @Override
    public PurchasesApi.Purchase toDTO(Purchase model) {
        return model == null ? null : new PurchasesApi.Purchase(
                model.purchaseId() == null ? null : model.purchaseId().value(),
                model.owner().value(),
                model.receiptId() == null ? null : model.receiptId().value(),
                model.name(),
                model.dateTime(),
                money(model.price()),
                model.quantity().doubleValue(),
                model.category() == null ? null : model.category().categoryId().value()
        );
    }
}
