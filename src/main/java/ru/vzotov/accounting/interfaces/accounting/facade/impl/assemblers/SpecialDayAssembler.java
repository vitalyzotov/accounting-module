package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.calendar.domain.model.SpecialDay;

public class SpecialDayAssembler {

    public static AccountingApi.SpecialDay toDTO(SpecialDay domain) {
        return domain == null ? null : new AccountingApi.SpecialDay(
                domain.day(),
                domain.name(),
                String.valueOf(domain.type().symbol())
        );
    }
}
