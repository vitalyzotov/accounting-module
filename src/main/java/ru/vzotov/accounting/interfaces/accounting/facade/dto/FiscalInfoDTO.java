package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.io.Serializable;

public record FiscalInfoDTO(String kktNumber, String kktRegId, String fiscalSign, String fiscalDocumentNumber,
                            String fiscalDriveNumber) implements Serializable {
}
