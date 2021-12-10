package ru.vzotov.accounting.interfaces.purchases.facade.impl.assembler;

import ru.vzotov.accounting.interfaces.common.assembler.AbstractAssembler;
import ru.vzotov.accounting.interfaces.common.assembler.MoneyDTOAssembler;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;
import ru.vzotov.purchase.domain.model.Purchase;

public class PurchaseDTOAssembler extends AbstractAssembler<PurchaseDTO, Purchase> {

    private final MoneyDTOAssembler moneyAssembler = new MoneyDTOAssembler();

    @Override
    public PurchaseDTO toDTO(Purchase model) {
        PurchaseDTO dto = new PurchaseDTO();
        dto.setPurchaseId(model.purchaseId() == null ? null : model.purchaseId().value());
        dto.setCheckId(model.checkId() == null ? null : model.checkId().value());
        dto.setName(model.name());
        dto.setDateTime(model.dateTime());
        dto.setPrice(moneyAssembler.toDTO(model.price()));
        dto.setQuantity(model.quantity().doubleValue());
        dto.setCategoryId(model.category() == null ? null : model.category().categoryId().value());
        return dto;
    }
}
