package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.banking.domain.model.MccCode;
import ru.vzotov.banking.domain.model.MccGroupId;

import java.util.List;

public interface MccFacade {
    List<AccountingApi.MccGroup> listGroups();

    AccountingApi.MccGroup getGroup(MccGroupId groupId);

    AccountingApi.MccDetails getDetails(MccCode code);

    List<AccountingApi.MccDetails> listGroupDetails(MccGroupId groupId);

    List<AccountingApi.MccDetails> listDetails();
}
