package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.MccGroupDTO;
import ru.vzotov.banking.domain.model.MccGroup;

public class MccGroupDTOAssembler {

    public static MccGroupDTO toDTO(MccGroup group) {
        return group == null ? null : new MccGroupDTO(
                group.groupId().value(),
                group.name()
        );
    }
}
