package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi.MccDetails;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.MccGroup;
import ru.vzotov.banking.domain.model.MccCode;
import ru.vzotov.banking.domain.model.MccGroupId;

import java.util.List;

public interface MccFacade {
    List<MccGroup> listGroups();

    MccGroup getGroup(MccGroupId groupId);

    MccDetails getDetails(MccCode code);

    List<MccDetails> listGroupDetails(MccGroupId groupId);

    List<MccDetails> listDetails();
}
