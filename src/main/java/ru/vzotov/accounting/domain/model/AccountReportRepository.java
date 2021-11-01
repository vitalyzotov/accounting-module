package ru.vzotov.accounting.domain.model;

import java.util.List;

public interface AccountReportRepository<I extends AccountReportOperation> {
    AccountReport<I> find(AccountReportId reportId);

    List<AccountReportId> findAll();

    List<AccountReportId> findUnprocessed();

    void markProcessed(AccountReportId reportId);
}
