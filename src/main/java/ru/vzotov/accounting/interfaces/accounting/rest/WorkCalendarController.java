package ru.vzotov.accounting.interfaces.accounting.rest;

import ru.vzotov.accounting.interfaces.accounting.facade.WorkCalendarFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.WorkCalendarDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/accounting/calendar")
@CrossOrigin
public class WorkCalendarController {

    @Autowired
    private WorkCalendarFacade workCalendarFacade;

    @GetMapping("{location}")
    public WorkCalendarDTO getCalendar(
            @PathVariable String location,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return workCalendarFacade.getCalendar(location, from, to);
    }

}
