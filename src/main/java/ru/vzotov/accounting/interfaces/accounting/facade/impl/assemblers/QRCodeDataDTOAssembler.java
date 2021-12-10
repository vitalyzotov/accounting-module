package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.QRCodeDataDTO;
import ru.vzotov.accounting.interfaces.common.assembler.AbstractAssembler;
import ru.vzotov.accounting.interfaces.common.assembler.MoneyDTOAssembler;

public class QRCodeDataDTOAssembler extends AbstractAssembler<QRCodeDataDTO, QRCodeData> {

    public QRCodeDataDTO toDTO(QRCodeData data) {
        MoneyDTOAssembler money = new MoneyDTOAssembler();
        return new QRCodeDataDTO(
                data.dateTime().value(),
                money.toDTO(data.totalSum()),
                data.fiscalDriveNumber(),
                data.fiscalDocumentNumber(),
                data.fiscalSign().toString(),
                data.operationType().numericValue()
        );
    }

}
