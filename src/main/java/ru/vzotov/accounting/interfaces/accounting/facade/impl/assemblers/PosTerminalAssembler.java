package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.banking.domain.model.City;
import ru.vzotov.banking.domain.model.PosTerminal;
import ru.vzotov.banking.domain.model.Street;

import java.util.Optional;

public class PosTerminalAssembler {

    public static AccountingApi.PosTerminal toDTO(PosTerminal model) {
        return model == null ? null : new AccountingApi.PosTerminal(
                model.terminalId().value(),
                model.country().code(),
                Optional.ofNullable(model.city()).map(City::name).orElse(null),
                Optional.ofNullable(model.street()).map(Street::name).orElse(null),
                model.merchant().name()
        );
    }
}
