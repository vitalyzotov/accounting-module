package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.common.assembler.AbstractAssembler;
import ru.vzotov.accounting.interfaces.common.assembler.MoneyDTOAssembler;
import ru.vzotov.cashreceipt.domain.model.Receipt;

public class ReceiptDTOAssembler extends AbstractAssembler<AccountingApi.Receipt, Receipt> {

    @Override
    public AccountingApi.Receipt toDTO(Receipt receipt) {
        if (receipt == null) return null;
        MoneyDTOAssembler moneyDTOAssembler = new MoneyDTOAssembler();
        return new AccountingApi.Receipt(
                receipt.owner().value(),
                receipt.receiptId().value(),
                new FiscalInfoDTOAssembler().toDTO(receipt.fiscalInfo()),
                receipt.dateTime(),
                receipt.requestNumber(),
                new ItemDTOAssembler().toDTOList(receipt.products().items()),
                new ItemDTOAssembler().toDTOList(receipt.products().stornoItems()),
                moneyDTOAssembler.toDTO(receipt.products().totalSum()),
                moneyDTOAssembler.toDTO(receipt.paymentInfo().cash()),
                moneyDTOAssembler.toDTO(receipt.paymentInfo().eCash()),
                receipt.marketing() == null ? null : moneyDTOAssembler.toDTO(receipt.marketing().markup()),
                receipt.marketing() == null ? null : moneyDTOAssembler.toDTO(receipt.marketing().markupSum()),
                receipt.marketing() == null ? null : moneyDTOAssembler.toDTO(receipt.marketing().discount()),
                receipt.marketing() == null ? null : moneyDTOAssembler.toDTO(receipt.marketing().discountSum()),
                receipt.shiftInfo() == null ? null : receipt.shiftInfo().shiftNumber(),
                receipt.shiftInfo() == null ? null : receipt.shiftInfo().operator(),
                receipt.retailPlace() == null ? null : receipt.retailPlace().user(),
                receipt.retailPlace() == null || receipt.retailPlace().userInn() == null ? null : receipt.retailPlace().userInn().value(),
                receipt.retailPlace() == null || receipt.retailPlace().address() == null ? null : receipt.retailPlace().address().value(),
                receipt.retailPlace() == null ? null : receipt.retailPlace().taxationType(),
                receipt.operationType().numericValue()
        );
    }

}
