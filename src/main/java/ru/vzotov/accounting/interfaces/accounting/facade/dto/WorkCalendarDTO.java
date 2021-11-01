package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;
import java.util.List;

public class WorkCalendarDTO {
    private LocalDate from;
    private LocalDate to;
    private String location;
    private List<SpecialDayDTO> specialDays;

    public WorkCalendarDTO() {
    }

    public WorkCalendarDTO(LocalDate from, LocalDate to, String location, List<SpecialDayDTO> specialDays) {
        this.from = from;
        this.to = to;
        this.location = location;
        this.specialDays = specialDays;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<SpecialDayDTO> getSpecialDays() {
        return specialDays;
    }

    public void setSpecialDays(List<SpecialDayDTO> specialDays) {
        this.specialDays = specialDays;
    }
}
