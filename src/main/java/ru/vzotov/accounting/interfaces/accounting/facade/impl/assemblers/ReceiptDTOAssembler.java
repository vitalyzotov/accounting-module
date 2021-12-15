package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.ReceiptRef;
import ru.vzotov.cashreceipt.domain.model.Check;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.ReceiptDTO;
import ru.vzotov.accounting.interfaces.common.assembler.AbstractAssembler;
import ru.vzotov.accounting.interfaces.common.assembler.MoneyDTOAssembler;

public class ReceiptDTOAssembler extends AbstractAssembler<ReceiptDTO, Check> {

    @Override
    public ReceiptDTO toDTO(Check check) {
        if (check == null) return null;
        MoneyDTOAssembler moneyDTOAssembler = new MoneyDTOAssembler();
        return new ReceiptDTO(
                check.checkId().value(),
                new FiscalInfoDTOAssembler().toDTO(check.fiscalInfo()),
                check.dateTime(),
                check.requestNumber(),
                new ItemDTOAssembler().toDTOList(check.products().items()),
                new ItemDTOAssembler().toDTOList(check.products().stornoItems()),
                moneyDTOAssembler.toDTO(check.products().totalSum()),
                moneyDTOAssembler.toDTO(check.paymentInfo().cash()),
                moneyDTOAssembler.toDTO(check.paymentInfo().eCash()),
                check.marketing() == null ? null : moneyDTOAssembler.toDTO(check.marketing().markup()),
                check.marketing() == null ? null : moneyDTOAssembler.toDTO(check.marketing().markupSum()),
                check.marketing() == null ? null : moneyDTOAssembler.toDTO(check.marketing().discount()),
                check.marketing() == null ? null : moneyDTOAssembler.toDTO(check.marketing().discountSum()),
                check.shiftInfo() == null ? null : check.shiftInfo().shiftNumber(),
                check.shiftInfo() == null ? null : check.shiftInfo().operator(),
                check.retailPlace() == null ? null : check.retailPlace().user(),
                check.retailPlace() == null || check.retailPlace().userInn() == null ? null : check.retailPlace().userInn().value(),
                check.retailPlace() == null || check.retailPlace().address() == null ? null : check.retailPlace().address().value(),
                check.retailPlace() == null ? null : check.retailPlace().taxationType(),
                check.operationType().numericValue()
        );
    }

}
