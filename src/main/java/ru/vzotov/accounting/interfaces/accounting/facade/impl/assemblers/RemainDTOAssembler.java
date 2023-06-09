package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.domain.model.Remain;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;

public class RemainDTOAssembler {

    public static AccountingApi.Remain toDTO(Remain remain) {
        return remain == null ? null : new AccountingApi.Remain(
                remain.remainId().value(),
                remain.date(),
                remain.account().number(),
                MoneyDTOAssembler.toDTO(remain.value())
        );
    }
}
