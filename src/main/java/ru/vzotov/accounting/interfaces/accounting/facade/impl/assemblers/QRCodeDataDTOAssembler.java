package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.common.assembler.AbstractAssembler;
import ru.vzotov.accounting.interfaces.common.assembler.MoneyDTOAssembler;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;

public class QRCodeDataDTOAssembler extends AbstractAssembler<AccountingApi.QRCodeData, QRCodeData> {

    public AccountingApi.QRCodeData toDTO(QRCodeData data) {
        MoneyDTOAssembler money = new MoneyDTOAssembler();
        return new AccountingApi.QRCodeData(
                data.dateTime().value(),
                money.toDTO(data.totalSum()),
                data.fiscalDriveNumber(),
                data.fiscalDocumentNumber(),
                data.fiscalSign().toString(),
                data.operationType().numericValue()
        );
    }

}
