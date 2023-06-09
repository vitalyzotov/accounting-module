package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.domain.model.Money;

public class MoneyDTOAssembler {

    public static AccountingApi.Money toDTO(Money money) {
        return money == null ? null : new AccountingApi.Money(money.rawAmount(), money.currency().getCurrencyCode());
    }
}
