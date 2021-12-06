package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.vzotov.accounting.application.AccountNotFoundException;
import ru.vzotov.accounting.application.AccountReportNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.AccountReportsFacade;
import ru.vzotov.banking.domain.model.BankId;

import java.io.IOException;

@RestController
@RequestMapping("/accounting/reports")
@CrossOrigin
public class AccountReportController {

    private final AccountReportsFacade accountReportsFacade;

    public AccountReportController(AccountReportsFacade accountReportsFacade) {
        this.accountReportsFacade = accountReportsFacade;
    }

    @PostMapping("{bank}")
    public void importReport(@PathVariable String bank, @RequestParam("report") MultipartFile uploadedReport)
            throws IOException, AccountReportNotFoundException, AccountNotFoundException {
        final BankId bankId = new BankId(bank);
        accountReportsFacade.importReport(bankId.value(),
                uploadedReport.getOriginalFilename(), uploadedReport.getInputStream());
    }
}
