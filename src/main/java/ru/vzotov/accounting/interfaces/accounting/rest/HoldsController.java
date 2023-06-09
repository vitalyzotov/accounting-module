package ru.vzotov.accounting.interfaces.accounting.rest;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.OperationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.banking.domain.model.OperationType;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/accounting/holds")
@CrossOrigin
public class HoldsController {

    @Autowired
    private AccountingFacade accountingFacade;

    @GetMapping
    public List<AccountingApi.HoldOperation> listHolds(@RequestParam(required = false) String type,
                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return accountingFacade.listHolds(
                type == null ? null : OperationType.of(type.charAt(0)),
                from, to);
    }

    @GetMapping("{holdId}")
    public AccountingApi.HoldOperation getOperation(@PathVariable String holdId) throws OperationNotFoundException {
        return accountingFacade.getHold(holdId);
    }

    @DeleteMapping("{holdId}")
    public void deleteOperation(@PathVariable String holdId) throws OperationNotFoundException {
        accountingFacade.deleteHold(holdId);
    }

}
