package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.MccDetailsDTO;
import ru.vzotov.banking.domain.model.MccDetails;

public class MccDetailsDTOAssembler {

    public static MccDetailsDTO toDTO(MccDetails details) {
        return details == null ? null : new MccDetailsDTO(
                details.mcc().value(),
                details.name(),
                details.group() == null ? null : details.group().groupId().value()
        );
    }
}
