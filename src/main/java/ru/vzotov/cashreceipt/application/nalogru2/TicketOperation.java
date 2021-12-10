package ru.vzotov.cashreceipt.application.nalogru2;

import java.time.OffsetDateTime;

public class TicketOperation {
    OffsetDateTime date;
    Long type;
    Long sum;

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }
}
