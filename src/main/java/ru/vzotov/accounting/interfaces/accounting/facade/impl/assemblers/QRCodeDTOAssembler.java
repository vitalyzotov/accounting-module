package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.ReceiptRef;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.cashreceipt.domain.model.QRCode;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.QRCodeDTO;
import ru.vzotov.accounting.interfaces.common.assembler.AbstractAssembler;

public class QRCodeDTOAssembler extends AbstractAssembler<QRCodeDTO, QRCode> {

    private final QRCodeDataDTOAssembler assembler = new QRCodeDataDTOAssembler();

    public ReceiptRef toRef(ReceiptId model) {
        return model == null? null : new ReceiptRef(model.value());
    }

    public ReceiptRef toRef(QRCode code) {
        return code == null? null: toRef(code.receiptId());
    }

    @Override
    public QRCodeDTO toDTO(QRCode code) {
        return code == null ? null : new QRCodeDTO(
                code.receiptId().value(),
                assembler.toDTO(code.code()),
                code.state().name(),
                code.loadingTryCount(),
                code.loadedAt(),
                code.owner().value()
        );
    }

}
