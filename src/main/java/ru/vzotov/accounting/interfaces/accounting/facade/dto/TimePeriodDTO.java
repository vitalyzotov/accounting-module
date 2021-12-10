package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;

public class TimePeriodDTO {
    private LocalDate fromDay;
    private LocalDate toDay;
    private long count;

    public TimePeriodDTO() {
    }

    public TimePeriodDTO(LocalDate fromDay, LocalDate toDay, long count) {
        this.fromDay = fromDay;
        this.toDay = toDay;
        this.count = count;
    }

    public LocalDate getFromDay() {
        return fromDay;
    }

    public void setFromDay(LocalDate fromDay) {
        this.fromDay = fromDay;
    }

    public LocalDate getToDay() {
        return toDay;
    }

    public void setToDay(LocalDate toDay) {
        this.toDay = toDay;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
