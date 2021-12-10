package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.util.ArrayList;
import java.util.List;

public class TimelineDTO {
    private List<TimePeriodDTO> periods = new ArrayList<>();

    public TimelineDTO() {
    }

    public List<TimePeriodDTO> getPeriods() {
        return periods;
    }

    public void setPeriods(List<TimePeriodDTO> periods) {
        this.periods = periods;
    }
}
