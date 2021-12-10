package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import ru.vzotov.accounting.interfaces.common.dto.MoneyDTO;

import java.time.LocalDateTime;

public class QRCodeDataDTO {

    private LocalDateTime dateTime;
    private MoneyDTO totalSum;
    private String fiscalDriveNumber;
    private String fiscalDocumentNumber;
    private String fiscalSign;
    private Long operationType;

    public QRCodeDataDTO() {
    }

    public QRCodeDataDTO(LocalDateTime dateTime, MoneyDTO totalSum, String fiscalDriveNumber, String fiscalDocumentNumber, String fiscalSign, Long operationType) {
        this.dateTime = dateTime;
        this.totalSum = totalSum;
        this.fiscalDriveNumber = fiscalDriveNumber;
        this.fiscalDocumentNumber = fiscalDocumentNumber;
        this.fiscalSign = fiscalSign;
        this.operationType = operationType;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public MoneyDTO getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(MoneyDTO totalSum) {
        this.totalSum = totalSum;
    }

    public String getFiscalDriveNumber() {
        return fiscalDriveNumber;
    }

    public void setFiscalDriveNumber(String fiscalDriveNumber) {
        this.fiscalDriveNumber = fiscalDriveNumber;
    }

    public String getFiscalDocumentNumber() {
        return fiscalDocumentNumber;
    }

    public void setFiscalDocumentNumber(String fiscalDocumentNumber) {
        this.fiscalDocumentNumber = fiscalDocumentNumber;
    }

    public String getFiscalSign() {
        return fiscalSign;
    }

    public void setFiscalSign(String fiscalSign) {
        this.fiscalSign = fiscalSign;
    }

    public Long getOperationType() {
        return operationType;
    }

    public void setOperationType(Long operationType) {
        this.operationType = operationType;
    }
}
