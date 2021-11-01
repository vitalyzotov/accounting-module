package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import ru.vzotov.accounting.domain.model.WorkCalendarRepository;
import ru.vzotov.accounting.interfaces.accounting.facade.WorkCalendarFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.WorkCalendarDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.WorkCalendarDTOAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class WorkCalendarFacadeImpl implements WorkCalendarFacade {

    @Autowired
    private WorkCalendarRepository workCalendarRepository;

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public WorkCalendarDTO getCalendar(String location, LocalDate from, LocalDate to) {
        return WorkCalendarDTOAssembler.toDTO(workCalendarRepository.find(location, from, to));
    }
}
