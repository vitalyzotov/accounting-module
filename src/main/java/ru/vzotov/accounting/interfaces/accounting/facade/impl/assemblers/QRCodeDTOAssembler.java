package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.ReceiptRef;
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.CheckQRCode;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.QRCodeDTO;
import ru.vzotov.accounting.interfaces.common.assembler.AbstractAssembler;

public class QRCodeDTOAssembler extends AbstractAssembler<QRCodeDTO, CheckQRCode> {

    private final QRCodeDataDTOAssembler assembler = new QRCodeDataDTOAssembler();

    public ReceiptRef toRef(CheckId model) {
        return model == null? null : new ReceiptRef(model.value());
    }

    public ReceiptRef toRef(CheckQRCode code) {
        return code == null? null: toRef(code.checkId());
    }

    @Override
    public QRCodeDTO toDTO(CheckQRCode code) {
        return code == null ? null : new QRCodeDTO(
                code.checkId().value(),
                assembler.toDTO(code.code()),
                code.state().name(),
                code.loadingTryCount(),
                code.loadedAt()
        );
    }

}
