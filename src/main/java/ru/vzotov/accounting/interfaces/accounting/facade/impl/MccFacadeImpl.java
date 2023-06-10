package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.domain.model.MccDetailsRepository;
import ru.vzotov.accounting.domain.model.MccGroupRepository;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.accounting.facade.MccFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.MccDetailsAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.MccGroupAssembler;
import ru.vzotov.banking.domain.model.MccCode;
import ru.vzotov.banking.domain.model.MccGroupId;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MccFacadeImpl implements MccFacade {

    private static final String MCC_DATASOURCE = "accounting-tx";

    private final MccDetailsRepository mccDetailsRepository;

    private final MccGroupRepository mccGroupRepository;

    public MccFacadeImpl(MccDetailsRepository mccDetailsRepository,
                         MccGroupRepository mccGroupRepository) {
        this.mccDetailsRepository = mccDetailsRepository;
        this.mccGroupRepository = mccGroupRepository;
    }

    @Override
    @Transactional(value = MCC_DATASOURCE, readOnly = true)
    public List<AccountingApi.MccGroup> listGroups() {
        return mccGroupRepository.findAll()
                .stream()
                .map(MccGroupAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = MCC_DATASOURCE, readOnly = true)
    public AccountingApi.MccGroup getGroup(MccGroupId groupId) {
        Validate.notNull(groupId);
        return MccGroupAssembler.toDTO(mccGroupRepository.find(groupId));
    }

    @Override
    @Transactional(value = MCC_DATASOURCE, readOnly = true)
    public AccountingApi.MccDetails getDetails(MccCode code) {
        Validate.notNull(code);
        return MccDetailsAssembler.toDTO(mccDetailsRepository.find(code));
    }

    @Override
    @Transactional(value = MCC_DATASOURCE, readOnly = true)
    public List<AccountingApi.MccDetails> listGroupDetails(MccGroupId groupId) {
        Validate.notNull(groupId);
        return mccDetailsRepository.findByGroup(groupId)
                .stream()
                .map(MccDetailsAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = MCC_DATASOURCE, readOnly = true)
    public List<AccountingApi.MccDetails> listDetails() {
        return mccDetailsRepository.findAll()
                .stream()
                .map(MccDetailsAssembler::toDTO)
                .collect(Collectors.toList());
    }
}
