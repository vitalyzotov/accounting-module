package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.common.assembler.AbstractAssembler;
import ru.vzotov.accounting.interfaces.common.assembler.MoneyAssembler;
import ru.vzotov.cashreceipt.domain.model.Item;

public class ItemAssembler extends AbstractAssembler<AccountingApi.Item, Item> {

    @Override
    public AccountingApi.Item toDTO(Item item) {
        MoneyAssembler moneyAssembler = new MoneyAssembler();
        return item == null ? null :
                new AccountingApi.Item(
                        item.name(),
                        moneyAssembler.toDTO(item.price()),
                        item.quantity(),
                        moneyAssembler.toDTO(item.sum()),
                        item.index(),
                        item.category() == null ? null : item.category().name());//todo: тут наверное нужно возвращать ID?
    }

}
