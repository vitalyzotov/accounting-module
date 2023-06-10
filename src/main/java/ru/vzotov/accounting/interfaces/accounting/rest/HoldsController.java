package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.HoldOperation;
import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.OperationNotFoundException;
import ru.vzotov.banking.domain.model.OperationType;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/accounting/holds")
@CrossOrigin
public class HoldsController {

    private final AccountingFacade accountingFacade;

    public HoldsController(AccountingFacade accountingFacade) {
        this.accountingFacade = accountingFacade;
    }

    @GetMapping
    public List<HoldOperation> listHolds(@RequestParam(required = false) String type,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return accountingFacade.listHolds(
                type == null ? null : OperationType.of(type.charAt(0)),
                from, to);
    }

    @GetMapping("{holdId}")
    public HoldOperation getOperation(@PathVariable String holdId) throws OperationNotFoundException {
        return accountingFacade.getHold(holdId);
    }

    @DeleteMapping("{holdId}")
    public void deleteOperation(@PathVariable String holdId) throws OperationNotFoundException {
        accountingFacade.deleteHold(holdId);
    }

}
