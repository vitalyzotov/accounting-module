package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.MccDetailsDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.MccGroupDTO;
import ru.vzotov.banking.domain.model.MccCode;
import ru.vzotov.banking.domain.model.MccGroupId;

import java.util.List;

public interface MccFacade {
    List<MccGroupDTO> listGroups();

    MccGroupDTO getGroup(MccGroupId groupId);

    MccDetailsDTO getDetails(MccCode code);

    List<MccDetailsDTO> listGroupDetails(MccGroupId groupId);

    List<MccDetailsDTO> listDetails();
}
