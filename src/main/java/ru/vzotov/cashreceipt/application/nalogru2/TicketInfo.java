package ru.vzotov.cashreceipt.application.nalogru2;

import java.time.OffsetDateTime;
import java.util.List;

public class TicketInfo {
    String id;
    Long status;
    String kind;
    OffsetDateTime createdAt;
    StatusDescription statusDescription;
    String qr;
    TicketOperation operation;
    List<Process> process;
    TicketQuery query;
    TicketData ticket;
    Organization organization;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public StatusDescription getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(StatusDescription statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public TicketOperation getOperation() {
        return operation;
    }

    public void setOperation(TicketOperation operation) {
        this.operation = operation;
    }

    public List<Process> getProcess() {
        return process;
    }

    public void setProcess(List<Process> process) {
        this.process = process;
    }

    public TicketQuery getQuery() {
        return query;
    }

    public void setQuery(TicketQuery query) {
        this.query = query;
    }

    public TicketData getTicket() {
        return ticket;
    }

    public void setTicket(TicketData ticket) {
        this.ticket = ticket;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
