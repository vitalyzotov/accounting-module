package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.io.Serializable;

public final class FiscalInfoDTO implements Serializable {
    private String kktNumber;
    private String kktRegId;
    private String fiscalSign;
    private String fiscalDocumentNumber;
    private String fiscalDriveNumber;

    public FiscalInfoDTO() {
    }

    public FiscalInfoDTO(String kktNumber, String kktRegId, String fiscalSign, String fiscalDocumentNumber, String fiscalDriveNumber) {
        this.kktNumber = kktNumber;
        this.kktRegId = kktRegId;
        this.fiscalSign = fiscalSign;
        this.fiscalDocumentNumber = fiscalDocumentNumber;
        this.fiscalDriveNumber = fiscalDriveNumber;
    }

    public String getKktNumber() {
        return kktNumber;
    }

    public String getKktRegId() {
        return kktRegId;
    }

    public String getFiscalSign() {
        return fiscalSign;
    }

    public String getFiscalDocumentNumber() {
        return fiscalDocumentNumber;
    }

    public String getFiscalDriveNumber() {
        return fiscalDriveNumber;
    }
}
