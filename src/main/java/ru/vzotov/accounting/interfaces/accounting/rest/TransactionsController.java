package ru.vzotov.accounting.interfaces.accounting.rest;

import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.banking.domain.model.OperationId;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/accounting/tx")
@CrossOrigin
public class TransactionsController {
    @Autowired
    private AccountingFacade accountingFacade;

    @GetMapping
    public List<TransactionDTO> listTransactions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Integer threshold) {
        return accountingFacade.listTransactions(from, to, threshold);
    }

    @PostMapping
    public TransactionDTO makeTransaction(@RequestBody TransactionDTO transaction) {
        return accountingFacade.makeTransaction(
                new OperationId(transaction.getPrimary()),
                new OperationId(transaction.getSecondary())
        );
    }

}
