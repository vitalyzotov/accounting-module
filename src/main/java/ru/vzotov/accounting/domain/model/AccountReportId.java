package ru.vzotov.accounting.domain.model;

import ru.vzotov.ddd.shared.ValueObject;

import java.time.Instant;
import java.util.Objects;

public class AccountReportId implements ValueObject<AccountReportId> {

    private String name;

    private Instant date;

    public AccountReportId(String name, Instant date) {
        this.name = name;
        this.date = date;
    }

    public String name() {
        return name;
    }

    public Instant date() {
        return date;
    }

    @Override
    public boolean sameValueAs(AccountReportId that) {
        return that != null && Objects.equals(name, that.name) &&
                Objects.equals(date, that.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountReportId that = (AccountReportId) o;
        return sameValueAs(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date);
    }

    @Override
    public String toString() {
        return "report{" + name + ", " + date + '}';
    }
}
