package ru.vzotov.accounting.interfaces.purchases.facade.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.DEDUCTION;

@JsonAutoDetect(fieldVisibility = ANY)
@JsonTypeInfo(use = DEDUCTION, defaultImpl = PurchaseIdDTO.class)
@JsonSubTypes({@JsonSubTypes.Type(PurchaseIdDTO.class), @JsonSubTypes.Type(PurchaseDTO.class)})

public interface PurchaseRef {
    String purchaseId();
}
