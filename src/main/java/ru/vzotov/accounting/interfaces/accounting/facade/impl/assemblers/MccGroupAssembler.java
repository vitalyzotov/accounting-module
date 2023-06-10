package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.banking.domain.model.MccGroup;

public class MccGroupAssembler {

    public static AccountingApi.MccGroup toDTO(MccGroup group) {
        return group == null ? null : new AccountingApi.MccGroup(
                group.groupId().value(),
                group.name()
        );
    }
}
