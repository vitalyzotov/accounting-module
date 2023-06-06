package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import ru.vzotov.accounting.interfaces.common.dto.MoneyDTO;

import java.time.LocalDateTime;

public record QRCodeDataDTO(LocalDateTime dateTime, MoneyDTO totalSum, String fiscalDriveNumber,
                            String fiscalDocumentNumber, String fiscalSign, Long operationType) {

}
