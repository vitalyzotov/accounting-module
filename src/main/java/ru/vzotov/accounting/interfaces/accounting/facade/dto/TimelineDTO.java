package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.util.ArrayList;
import java.util.List;

public record TimelineDTO(List<TimePeriodDTO> periods) {
    public TimelineDTO() {
        this(new ArrayList<>());
    }
}
