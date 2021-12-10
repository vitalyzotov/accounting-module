package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.cashreceipt.domain.model.CheckQRCode;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.QRCodeDTO;
import ru.vzotov.accounting.interfaces.common.assembler.AbstractAssembler;

public class QRCodeDTOAssembler extends AbstractAssembler<QRCodeDTO, CheckQRCode> {

    private final QRCodeDataDTOAssembler assembler = new QRCodeDataDTOAssembler();

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
