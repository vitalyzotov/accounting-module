package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.OffsetDateTime;

public class QRCodeDTO {
    private String checkId;
    private QRCodeDataDTO data;
    private String state;
    private Long loadingTryCount;
    private OffsetDateTime loadedAt;

    public QRCodeDTO() {
    }

    public QRCodeDTO(String checkId, QRCodeDataDTO data, String state, Long loadingTryCount, OffsetDateTime loadedAt) {
        this.checkId = checkId;
        this.data = data;
        this.state = state;
        this.loadingTryCount = loadingTryCount;
        this.loadedAt = loadedAt;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
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
