package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.domain.model.Remain;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;

import static ru.vzotov.accounting.interfaces.common.assembler.MoneyAssembler.money;

public class RemainAssembler {

    public static AccountingApi.Remain toDTO(Remain remain) {
        return remain == null ? null : new AccountingApi.Remain(
                remain.remainId().value(),
                remain.date(),
                remain.account().number(),
                money(remain.value())
        );
    }
}
