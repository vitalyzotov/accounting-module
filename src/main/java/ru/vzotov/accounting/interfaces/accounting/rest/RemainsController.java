package ru.vzotov.accounting.interfaces.accounting.rest;

import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.RemainDTO;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.RemainCreateRequest;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.RemainCreateResponse;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.RemainDeleteResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounting/remains")
@CrossOrigin
public class RemainsController {

    @Autowired
    private AccountingFacade accountingFacade;

    @GetMapping
    public List<RemainDTO> listRemains(@RequestParam(name = "account", required = false) List<String> accounts,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (from != null && to != null) {
            List<RemainDTO> result = accountingFacade.listRemains(from, to);
            return result.stream().filter(dto -> accounts == null || accounts.isEmpty() || accounts.contains(dto.getAccountNumber())).collect(Collectors.toList());
        } else {
            return accounts.stream().flatMap(account -> accountingFacade.listRemains(account).stream()).collect(Collectors.toList());
        }
    }

    @GetMapping("{remainId}")
    public RemainDTO getRemain(@PathVariable String remainId) {
        return accountingFacade.getRemain(remainId);
    }

    @DeleteMapping("{remainId}")
    public RemainDeleteResponse deleteRemainById(@PathVariable String remainId) {
        accountingFacade.deleteRemain(remainId);
        return new RemainDeleteResponse(remainId);
    }

    @PostMapping
    public RemainCreateResponse newRemain(@RequestBody RemainCreateRequest remain) {
        String remainId = accountingFacade.createRemain(remain.getAccountNumber(), remain.getDate(), remain.getValue());
        return new RemainCreateResponse(remainId);
    }


}
