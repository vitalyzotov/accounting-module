package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.PosTerminalDTO;
import ru.vzotov.banking.domain.model.City;
import ru.vzotov.banking.domain.model.PosTerminal;
import ru.vzotov.banking.domain.model.Street;

import java.util.Optional;

public class PosTerminalDTOAssembler {

    public static PosTerminalDTO toDTO(PosTerminal model) {
        return model == null ? null : new PosTerminalDTO(
                model.terminalId().value(),
                model.country().code(),
                Optional.ofNullable(model.city()).map(City::name).orElse(null),
                Optional.ofNullable(model.street()).map(Street::name).orElse(null),
                model.merchant().name()
        );
    }
}
