package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.RemainDTO;
import ru.vzotov.accounting.domain.model.Remain;

public class RemainDTOAssembler {

    public static RemainDTO toDTO(Remain remain) {
        return remain == null ? null : new RemainDTO(
                remain.remainId().value(),
                remain.date(),
                remain.account().number(),
                MoneyDTOAssembler.toDTO(remain.value())
        );
    }
}
