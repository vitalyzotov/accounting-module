package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.Remain;
import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/accounting/remains")
@CrossOrigin
public class RemainsController {

    private final AccountingFacade accountingFacade;

    public RemainsController(AccountingFacade accountingFacade) {
        this.accountingFacade = accountingFacade;
    }

    @GetMapping
    public List<Remain> listRemains(
            @RequestParam(name = "account", required = false) List<String> accounts,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false, defaultValue = "false") boolean recent) {
        return accountingFacade.listRemains(accounts, from, to, recent);
    }

    @GetMapping("{remainId}")
    public Remain getRemain(@PathVariable String remainId) {
        return accountingFacade.getRemain(remainId);
    }

    @DeleteMapping("{remainId}")
    public Remain.Ref deleteRemainById(@PathVariable String remainId) {
        accountingFacade.deleteRemain(remainId);
        return new Remain.Ref(remainId);
    }

    @PostMapping
    public Remain.Ref newRemain(@RequestBody Remain.Create remain) {
        String remainId = accountingFacade.createRemain(remain.accountNumber(), remain.date(), remain.value());
        return new Remain.Ref(remainId);
    }


}
