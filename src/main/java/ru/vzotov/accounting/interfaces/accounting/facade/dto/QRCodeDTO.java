package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.OffsetDateTime;

public record QRCodeDTO(String receiptId, QRCodeDataDTO data, String state, Long loadingTryCount,
                        OffsetDateTime loadedAt, String owner) implements ReceiptRef {
}
