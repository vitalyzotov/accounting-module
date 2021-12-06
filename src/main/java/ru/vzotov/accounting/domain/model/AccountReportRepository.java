package ru.vzotov.accounting.domain.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface AccountReportRepository<I extends AccountReportOperation> {
    AccountReport<I> find(AccountReportId reportId);

    List<AccountReportId> findAll();

    List<AccountReportId> findUnprocessed();

    void markProcessed(AccountReportId reportId);

    AccountReportId save(String name, InputStream content) throws IOException;
}
