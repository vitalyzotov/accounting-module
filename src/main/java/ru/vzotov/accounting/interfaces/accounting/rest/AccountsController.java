package ru.vzotov.accounting.interfaces.accounting.rest;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.AccountDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.AccountOperationDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.CategoryNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.HoldOperationDTO;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.AccountCreateRequest;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.AccountModifyRequest;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.AccountStoreResponse;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.OperationCreateRequest;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/accounting/accounts")
@CrossOrigin
public class AccountsController {

    @Autowired
    private AccountingFacade accountingFacade;

    @GetMapping
    public List<AccountDTO> listAccounts() {
        return accountingFacade.listAccounts();
    }

    @GetMapping("{accountNumber}")
    public AccountDTO getAccount(@PathVariable String accountNumber) {
        return accountingFacade.getAccount(accountNumber);
    }

    @PostMapping
    public AccountStoreResponse newAccount(@RequestBody AccountCreateRequest account) {
        String accountNumber = accountingFacade.createAccount(account.getNumber(), account.getName(), account.getBankId(), account.getCurrency(), account.getOwner(), account.getAliases());
        return new AccountStoreResponse(accountNumber);
    }

    @PutMapping("{accountNumber}")
    public AccountStoreResponse modifyAccount(@PathVariable String accountNumber, @RequestBody AccountModifyRequest account) {
        accountingFacade.modifyAccount(accountNumber, account.getName(), account.getBankId(), account.getCurrency(), account.getAliases());
        return new AccountStoreResponse(accountNumber);
    }

    @GetMapping("{accountNumber}/operations")
    public List<AccountOperationDTO> listOperations(@PathVariable String accountNumber,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return accountingFacade.listOperations(accountNumber, from, to);
    }

    @PostMapping("{accountNumber}/operations")
    public AccountOperationDTO createOperation(@PathVariable String accountNumber,
                                               @RequestBody OperationCreateRequest operation) throws CategoryNotFoundException {
        Validate.notNull(operation.getOperationType());
        return accountingFacade.createOperation(
                accountNumber,
                operation.getDate(),
                operation.getAuthorizationDate(),
                operation.getTransactionReference(),
                operation.getOperationType().charAt(0),
                operation.getAmount(),
                operation.getCurrency(),
                operation.getDescription(),
                operation.getComment(),
                operation.getCategoryId()
        );
    }

    @GetMapping("{accountNumber}/holds")
    public List<HoldOperationDTO> listHoldOperations(@PathVariable String accountNumber,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return accountingFacade.listHolds(accountNumber, from, to);
    }

}
