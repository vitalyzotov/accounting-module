package ru.vzotov.accounting.interfaces.common.assembler;

import ru.vzotov.accounting.interfaces.common.CommonApi;
import ru.vzotov.domain.model.Money;

public class MoneyAssembler extends AbstractAssembler<CommonApi.Money, Money> {
    @Override
    public CommonApi.Money toDTO(Money money) {
        return money == null ? null : new CommonApi.Money(money.rawAmount(), money.currency().getCurrencyCode());
    }

    public static CommonApi.Money money(Money money) {
        return money == null ? null : new CommonApi.Money(money.rawAmount(), money.currency().getCurrencyCode());
    }

}
