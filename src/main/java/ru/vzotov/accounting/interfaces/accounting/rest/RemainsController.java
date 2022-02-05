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
import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.RemainDTO;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.RemainCreateRequest;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.RemainCreateResponse;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.RemainDeleteResponse;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/accounting/remains")
@CrossOrigin
public class RemainsController {

    private final AccountingFacade accountingFacade;

    public RemainsController(AccountingFacade accountingFacade) {
        this.accountingFacade = accountingFacade;
    }

    @GetMapping
    public List<RemainDTO> listRemains(@RequestParam(name = "account", required = false) List<String> accounts,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (from != null && to != null) {
            final List<RemainDTO> result = accountingFacade.listRemains(from, to);
            final Set<String> filter = new HashSet<>(accounts == null ? Collections.emptySet() : accounts);
            final Stream<RemainDTO> stream = filter.isEmpty() ? result.stream() :
                    result.stream().filter(dto -> filter.contains(dto.getAccountNumber()));
            return stream.collect(Collectors.toList());
        } else {
            return accounts.stream()
                    .flatMap(account -> accountingFacade.listRemains(account).stream())
                    .collect(Collectors.toList());
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
