package ru.vzotov.accounting.domain.model;


import ru.vzotov.ddd.shared.Entity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AccountReport<I extends AccountReportOperation> implements Entity<AccountReport<I>> {

    private AccountReportId reportId;

    private List<I> operations;

    public AccountReport(AccountReportId reportId, List<I> operations) {
        this.reportId = reportId;
        this.operations = Collections.unmodifiableList(operations);
    }

    public AccountReportId reportId() {
        return reportId;
    }

    public List<I> operations() {
        return operations;
    }

    @Override
    public boolean sameIdentityAs(AccountReport<I> accountReport) {
        return accountReport != null && Objects.equals(this.reportId, accountReport.reportId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountReport<I> that = (AccountReport<I>) o;
        return sameIdentityAs(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportId);
    }
}
