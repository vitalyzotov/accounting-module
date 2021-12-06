package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.application.AccountNotFoundException;
import ru.vzotov.accounting.application.AccountReportNotFoundException;

import java.io.IOException;
import java.io.InputStream;

public interface AccountReportsFacade {
    void importReport(String bankId, String name, InputStream report) throws IOException,
            AccountReportNotFoundException, AccountNotFoundException;
}
