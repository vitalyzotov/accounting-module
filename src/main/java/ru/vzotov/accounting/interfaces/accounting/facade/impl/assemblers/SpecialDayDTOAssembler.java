package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.SpecialDayDTO;
import ru.vzotov.calendar.domain.model.SpecialDay;

public class SpecialDayDTOAssembler {

    public static SpecialDayDTO toDTO(SpecialDay domain) {
        return domain == null ? null : new SpecialDayDTO(
                domain.day(),
                domain.name(),
                String.valueOf(domain.type().symbol())
        );
    }
}
