package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import ru.vzotov.accounting.domain.model.MccDetailsRepository;
import ru.vzotov.accounting.domain.model.MccGroupRepository;
import ru.vzotov.accounting.interfaces.accounting.facade.MccFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.MccDetailsDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.MccGroupDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.MccDetailsDTOAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.MccGroupDTOAssembler;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.banking.domain.model.MccCode;
import ru.vzotov.banking.domain.model.MccGroupId;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MccFacadeImpl implements MccFacade {

    private static final String MCC_DATASOURCE = "accounting-tx";

    @Autowired
    private MccDetailsRepository mccDetailsRepository;

    @Autowired
    private MccGroupRepository mccGroupRepository;

    @Override
    @Transactional(value = MCC_DATASOURCE, readOnly = true)
    public List<MccGroupDTO> listGroups() {
        return mccGroupRepository.findAll()
                .stream()
                .map(MccGroupDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = MCC_DATASOURCE, readOnly = true)
    public MccGroupDTO getGroup(MccGroupId groupId) {
        Validate.notNull(groupId);
        return MccGroupDTOAssembler.toDTO(mccGroupRepository.find(groupId));
    }

    @Override
    @Transactional(value = MCC_DATASOURCE, readOnly = true)
    public MccDetailsDTO getDetails(MccCode code) {
        Validate.notNull(code);
        return MccDetailsDTOAssembler.toDTO(mccDetailsRepository.find(code));
    }

    @Override
    @Transactional(value = MCC_DATASOURCE, readOnly = true)
    public List<MccDetailsDTO> listGroupDetails(MccGroupId groupId) {
        Validate.notNull(groupId);
        return mccDetailsRepository.findByGroup(groupId)
                .stream()
                .map(MccDetailsDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = MCC_DATASOURCE, readOnly = true)
    public List<MccDetailsDTO> listDetails() {
        return mccDetailsRepository.findAll()
                .stream()
                .map(MccDetailsDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }
}
