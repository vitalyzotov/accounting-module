package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.domain.model.WorkCalendarRepository;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.accounting.facade.WorkCalendarFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.WorkCalendarAssembler;

import java.time.LocalDate;

@Service
public class WorkCalendarFacadeImpl implements WorkCalendarFacade {

    private final WorkCalendarRepository workCalendarRepository;

    public WorkCalendarFacadeImpl(WorkCalendarRepository workCalendarRepository) {
        this.workCalendarRepository = workCalendarRepository;
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public AccountingApi.WorkCalendar getCalendar(String location, LocalDate from, LocalDate to) {
        return WorkCalendarAssembler.toDTO(workCalendarRepository.find(location, from, to));
    }
}
