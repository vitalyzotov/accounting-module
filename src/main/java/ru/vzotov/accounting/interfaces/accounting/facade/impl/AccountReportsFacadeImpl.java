package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import ru.vzotov.accounting.application.AccountNotFoundException;
import ru.vzotov.accounting.application.AccountReportNotFoundException;
import ru.vzotov.accounting.application.AccountReportService;
import ru.vzotov.accounting.domain.model.AccountReportId;
import ru.vzotov.accounting.interfaces.accounting.facade.AccountReportsFacade;
import ru.vzotov.banking.domain.model.BankId;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AccountReportsFacadeImpl implements AccountReportsFacade {

    private final Map<BankId, AccountReportService> reportServices;

    public AccountReportsFacadeImpl(Set<AccountReportService> reportServices) {
        this.reportServices = reportServices.stream().collect(Collectors.toMap(AccountReportService::bankId, Function.identity()));
    }

    @Override
    public void importReport(String bankId, String name, InputStream report) throws IOException, AccountReportNotFoundException, AccountNotFoundException {
        Validate.notNull(bankId);
        Validate.notNull(name);
        Validate.notNull(report);

        final BankId id = new BankId(bankId);
        final AccountReportService service = reportServices.get(id);
        if (service == null) throw new IllegalArgumentException("Unknown bank ID: " + bankId);

        final AccountReportId reportId = service.save(Paths.get(name).normalize().getFileName().toString(), report);

        service.processAccountReport(reportId);
    }
}
