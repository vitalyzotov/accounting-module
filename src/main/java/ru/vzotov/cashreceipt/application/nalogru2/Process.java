package ru.vzotov.cashreceipt.application.nalogru2;

import java.time.OffsetDateTime;

public class Process {
    OffsetDateTime time;
    Long result;

    public OffsetDateTime getTime() {
        return time;
    }

    public void setTime(OffsetDateTime time) {
        this.time = time;
    }

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }
}
