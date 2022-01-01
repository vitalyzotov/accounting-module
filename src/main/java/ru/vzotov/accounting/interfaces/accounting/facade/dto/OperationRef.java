package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.DEDUCTION;

@JsonAutoDetect(fieldVisibility = ANY)
@JsonTypeInfo(use = DEDUCTION, defaultImpl = OperationRef.class)
@JsonSubTypes({@JsonSubTypes.Type(AccountOperationDTO.class), @JsonSubTypes.Type(CardOperationDTO.class)})
public class OperationRef {

    private String operationId;

    public OperationRef() {
    }

    public OperationRef(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OperationRef that = (OperationRef) o;

        return Objects.equals(operationId, that.operationId);
    }

    @Override
    public int hashCode() {
        return operationId != null ? operationId.hashCode() : 0;
    }
}
