package ru.vzotov.accounting.domain.model;

import ru.vzotov.banking.domain.model.MccGroup;
import ru.vzotov.banking.domain.model.MccGroupId;

import java.util.List;

public interface MccGroupRepository {
    MccGroup find(MccGroupId id);
    List<MccGroup> findAll();
}
