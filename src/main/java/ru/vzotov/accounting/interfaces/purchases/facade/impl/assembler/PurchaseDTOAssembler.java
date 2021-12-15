package ru.vzotov.accounting.interfaces.purchases.facade.impl.assembler;

import ru.vzotov.accounting.interfaces.common.assembler.AbstractAssembler;
import ru.vzotov.accounting.interfaces.common.assembler.MoneyDTOAssembler;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseRef;
import ru.vzotov.purchase.domain.model.Purchase;
import ru.vzotov.purchase.domain.model.PurchaseId;

public class PurchaseDTOAssembler extends AbstractAssembler<PurchaseDTO, Purchase> {

    private final MoneyDTOAssembler moneyAssembler = new MoneyDTOAssembler();

    public PurchaseRef toRef(PurchaseId model) {
        return model == null ? null : new PurchaseRef(model.value());
    }

    public PurchaseRef toRef(Purchase model) {
        return model == null ? null : toRef(model.purchaseId());
    }

    @Override
    public PurchaseDTO toDTO(Purchase model) {
        return model == null ? null : new PurchaseDTO(
                model.purchaseId() == null ? null : model.purchaseId().value(),
                model.checkId() == null ? null : model.checkId().value(),
                model.name(),
                model.dateTime(),
                moneyAssembler.toDTO(model.price()),
                model.quantity().doubleValue(),
                model.category() == null ? null : model.category().categoryId().value()
        );
    }
}
