package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.MoneyDTO;
import ru.vzotov.domain.model.Money;

public class MoneyDTOAssembler {

    public static MoneyDTO toDTO(Money money) {
        return money == null ? null : new MoneyDTO(money.rawAmount(), money.currency().getCurrencyCode());
    }
}
