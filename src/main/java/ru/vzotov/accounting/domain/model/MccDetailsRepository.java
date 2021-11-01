package ru.vzotov.accounting.domain.model;

import ru.vzotov.banking.domain.model.MccCode;
import ru.vzotov.banking.domain.model.MccDetails;
import ru.vzotov.banking.domain.model.MccGroupId;

import java.util.List;

public interface MccDetailsRepository {
    MccDetails find(MccCode code);
    List<MccDetails> findByGroup(MccGroupId groupId);
    List<MccDetails> findAll();
}
