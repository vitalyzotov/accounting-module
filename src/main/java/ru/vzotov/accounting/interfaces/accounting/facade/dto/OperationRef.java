package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.DEDUCTION;

@JsonAutoDetect(fieldVisibility = ANY)
@JsonTypeInfo(use = DEDUCTION, defaultImpl = OperationIdDTO.class)
@JsonSubTypes({@JsonSubTypes.Type(OperationIdDTO.class), @JsonSubTypes.Type(AccountOperationDTO.class), @JsonSubTypes.Type(CardOperationDTO.class)})
public interface OperationRef {
     String operationId();
}
