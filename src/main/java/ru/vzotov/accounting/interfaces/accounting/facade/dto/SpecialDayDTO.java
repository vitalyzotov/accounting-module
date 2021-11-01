package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public class SpecialDayDTO {
    private LocalDate day;
    private String name;
    private String type;

    public SpecialDayDTO() {
    }

    public SpecialDayDTO(LocalDate day, String name, String type) {
        this.day = day;
        this.name = name;
        this.type = type;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
