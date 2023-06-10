package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.Bank;
import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
import ru.vzotov.banking.domain.model.BankId;

import java.util.List;

@RestController
@RequestMapping("/accounting/banks")
@CrossOrigin
public class BanksController {

    private final AccountingFacade accountingFacade;

    public BanksController(AccountingFacade accountingFacade) {
        this.accountingFacade = accountingFacade;
    }

    @GetMapping
    public List<Bank> listBanks() {
        return accountingFacade.listBanks();
    }

    @GetMapping("{bankId}")
    public Bank getBank(@PathVariable String bankId) {
        return accountingFacade.getBank(new BankId(bankId));
    }

    @PostMapping
    public Bank.Ref createBank(@RequestBody Bank.Create bank) {
        BankId bankId = accountingFacade.createBank(new BankId(bank.bankId()),
                bank.name(), bank.shortName(), bank.longName());
        return new Bank.Ref(bankId.value());
    }

    @PutMapping("{bankId}")
    public Bank.Ref modifyBank(@PathVariable String bankId, @RequestBody Bank.Modify bank) {
        final BankId id = accountingFacade.modifyBank(new BankId(bankId),
                bank.name(), bank.shortName(), bank.longName());
        return new Bank.Ref(id.value());
    }

}
