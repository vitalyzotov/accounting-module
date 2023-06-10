package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.banking.domain.model.BudgetCategory;

public class BudgetCategoryAssembler {

    public static AccountingApi.BudgetCategory toDTO(BudgetCategory category) {
        return category == null ? null :
                new AccountingApi.BudgetCategory(category.id().id(), category.owner().value(), category.name(),
                        ColorAssembler.toDTO(category.color()), category.icon());
    }
}
