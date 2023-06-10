package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.common.assembler.AbstractAssembler;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategory;

public class PurchaseCategoryAssembler extends AbstractAssembler<AccountingApi.PurchaseCategory, PurchaseCategory> {

    @Override
    public AccountingApi.PurchaseCategory toDTO(PurchaseCategory model) {
        return model == null ? null : new AccountingApi.PurchaseCategory(
                model.categoryId().value(),
                model.owner().value(),
                model.name(), model.parentId() == null ? null : model.parentId().value()
        );
    }
}
