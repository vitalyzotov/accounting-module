package ru.vzotov.accounting.application;

import ru.vzotov.accounting.domain.model.AccountReportId;

public interface AccountReportService {

    void processAccountReport(AccountReportId reportId) throws AccountReportNotFoundException, AccountNotFoundException;

    void processNewReports();

}
