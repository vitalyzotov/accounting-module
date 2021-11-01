package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetCategoryDTO;
import ru.vzotov.banking.domain.model.BudgetCategory;

public class BudgetCategoryDTOAssembler {

    public static BudgetCategoryDTO toDTO(BudgetCategory category) {
        return category == null ? null :
                new BudgetCategoryDTO(category.id().id(), category.name(),
                        ColorAssembler.toDTO(category.color()), category.icon());
    }
}
