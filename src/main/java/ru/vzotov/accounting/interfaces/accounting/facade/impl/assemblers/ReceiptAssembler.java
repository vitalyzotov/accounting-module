package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.common.assembler.AbstractAssembler;
import ru.vzotov.accounting.interfaces.common.assembler.MoneyAssembler;
import ru.vzotov.cashreceipt.domain.model.Receipt;

public class ReceiptAssembler extends AbstractAssembler<AccountingApi.Receipt, Receipt> {

    @Override
    public AccountingApi.Receipt toDTO(Receipt receipt) {
        if (receipt == null) return null;
        MoneyAssembler moneyAssembler = new MoneyAssembler();
        return new AccountingApi.Receipt(
                receipt.owner().value(),
                receipt.receiptId().value(),
                new FiscalInfoAssembler().toDTO(receipt.fiscalInfo()),
                receipt.dateTime(),
                receipt.requestNumber(),
                new ItemAssembler().toDTOList(receipt.products().items()),
                new ItemAssembler().toDTOList(receipt.products().stornoItems()),
                moneyAssembler.toDTO(receipt.products().totalSum()),
                moneyAssembler.toDTO(receipt.paymentInfo().cash()),
                moneyAssembler.toDTO(receipt.paymentInfo().eCash()),
                receipt.marketing() == null ? null : moneyAssembler.toDTO(receipt.marketing().markup()),
                receipt.marketing() == null ? null : moneyAssembler.toDTO(receipt.marketing().markupSum()),
                receipt.marketing() == null ? null : moneyAssembler.toDTO(receipt.marketing().discount()),
                receipt.marketing() == null ? null : moneyAssembler.toDTO(receipt.marketing().discountSum()),
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
