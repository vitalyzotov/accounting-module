package ru.vzotov.accounting.application;

import ru.vzotov.accounting.domain.model.AccountReportId;
import ru.vzotov.banking.domain.model.BankId;

import java.io.IOException;
import java.io.InputStream;

public interface AccountReportService {

    /**
     * @return ID of bank whose reports can be processed by this service
     */
    BankId bankId();

    AccountReportId save(String name, InputStream content) throws IOException;

    void processAccountReport(AccountReportId reportId) throws AccountReportNotFoundException, AccountNotFoundException;

    void processNewReports();

}
