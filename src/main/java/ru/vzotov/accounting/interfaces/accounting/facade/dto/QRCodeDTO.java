package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.OffsetDateTime;

public class QRCodeDTO extends ReceiptRef {
    private QRCodeDataDTO data;
    private String state;
    private Long loadingTryCount;
    private OffsetDateTime loadedAt;

    public QRCodeDTO() {
    }

    public QRCodeDTO(String receiptId, QRCodeDataDTO data, String state, Long loadingTryCount, OffsetDateTime loadedAt) {
        super(receiptId);
        this.data = data;
        this.state = state;
        this.loadingTryCount = loadingTryCount;
        this.loadedAt = loadedAt;
    }

    public QRCodeDataDTO getData() {
        return data;
    }

    public void setData(QRCodeDataDTO data) {
        this.data = data;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getLoadingTryCount() {
        return loadingTryCount;
    }

    public void setLoadingTryCount(Long loadingTryCount) {
        this.loadingTryCount = loadingTryCount;
    }

    public OffsetDateTime getLoadedAt() {
        return loadedAt;
    }

    public void setLoadedAt(OffsetDateTime loadedAt) {
        this.loadedAt = loadedAt;
    }
}
