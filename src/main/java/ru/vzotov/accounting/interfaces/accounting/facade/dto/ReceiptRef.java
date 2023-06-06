package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.DEDUCTION;

@JsonAutoDetect(fieldVisibility = ANY)
@JsonTypeInfo(use = DEDUCTION, defaultImpl = ReceiptIdDTO.class)
@JsonSubTypes({@JsonSubTypes.Type(ReceiptIdDTO.class), @JsonSubTypes.Type(ReceiptDTO.class), @JsonSubTypes.Type(QRCodeDTO.class)})
public interface ReceiptRef {
    String receiptId();
}
