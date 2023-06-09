package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.banking.domain.model.MccDetails;

public class MccDetailsDTOAssembler {

    public static AccountingApi.MccDetails toDTO(MccDetails details) {
        return details == null ? null : new AccountingApi.MccDetails(
                details.mcc().value(),
                details.name(),
                details.group() == null ? null : details.group().groupId().value()
        );
    }
}
