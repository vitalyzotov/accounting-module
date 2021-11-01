package ru.vzotov.accounting.interfaces.accounting.rest;

import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BankDTO;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.BankCreateRequest;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.BankStoreResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.banking.domain.model.BankId;

import java.util.List;

@RestController
@RequestMapping("/accounting/banks")
@CrossOrigin
public class BanksController {

    @Autowired
    private AccountingFacade accountingFacade;

    @GetMapping
    public List<BankDTO> listBanks() {
        return accountingFacade.listBanks();
    }

    @GetMapping("{bankId}")
    public BankDTO getBank(@PathVariable String bankId) {
        return accountingFacade.getBank(new BankId(bankId));
    }

    @PostMapping
    public BankStoreResponse createBank(@RequestBody BankCreateRequest bank) {
        BankId bankId = accountingFacade.createBank(
                new BankId(bank.getBankId()),
                bank.getName(), bank.getShortName(), bank.getLongName());
        return new BankStoreResponse(bankId.value());
    }

    @PutMapping("{bankId}")
    public BankStoreResponse modifyBank(@PathVariable String bankId, @RequestBody BankCreateRequest bank) {
        BankId id = accountingFacade.modifyBank(
                new BankId(bankId),
                bank.getName(), bank.getShortName(), bank.getLongName());
        return new BankStoreResponse(id.value());
    }

}
