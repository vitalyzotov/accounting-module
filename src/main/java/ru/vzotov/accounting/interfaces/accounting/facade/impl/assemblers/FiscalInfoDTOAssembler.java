package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.common.assembler.AbstractAssembler;
import ru.vzotov.cashreceipt.domain.model.FiscalInfo;

public class FiscalInfoDTOAssembler extends AbstractAssembler<AccountingApi.FiscalInfo, FiscalInfo> {

    @Override
    public AccountingApi.FiscalInfo toDTO(FiscalInfo fiscalInfo) {
        if (fiscalInfo == null) return null;
        return new AccountingApi.FiscalInfo(
                fiscalInfo.kktNumber(),
                fiscalInfo.kktRegId(),
                String.valueOf(fiscalInfo.fiscalSign().value()),
                fiscalInfo.fiscalDocumentNumber(),
                fiscalInfo.fiscalDriveNumber()
        );
    }
}
