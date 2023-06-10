package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.common.assembler.AbstractAssembler;
import ru.vzotov.cashreceipt.domain.model.QRCode;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;

public class QRCodeAssembler extends AbstractAssembler<AccountingApi.QRCode, QRCode> {

    private final QRCodeDataAssembler assembler = new QRCodeDataAssembler();

    public AccountingApi.ReceiptRef toRef(ReceiptId model) {
        return model == null ? null : new AccountingApi.ReceiptId(model.value());
    }

    public AccountingApi.ReceiptRef toRef(QRCode code) {
        return code == null ? null : toRef(code.receiptId());
    }

    @Override
    public AccountingApi.QRCode toDTO(QRCode code) {
        return code == null ? null : new AccountingApi.QRCode(
                code.receiptId().value(),
                assembler.toDTO(code.code()),
                code.state().name(),
                code.loadingTryCount(),
                code.loadedAt(),
                code.owner().value()
        );
    }

}
