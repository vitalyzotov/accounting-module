package ru.vzotov.accounting.interfaces.accounting.facade.dto;

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
}
