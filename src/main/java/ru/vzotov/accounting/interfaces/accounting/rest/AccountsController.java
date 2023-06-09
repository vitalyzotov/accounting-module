package ru.vzotov.accounting.interfaces.accounting.rest;

import org.apache.commons.lang3.Validate;
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
import ru.vzotov.accounting.application.AccountNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.CategoryNotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounting/accounts")
@CrossOrigin
public class AccountsController {

    @Autowired
    private AccountingFacade accountingFacade;

    @GetMapping
    public List<AccountingApi.Account> listAccounts() {
        return accountingFacade.listAccounts();
    }

    @GetMapping("{accountNumber}")
    public AccountingApi.Account getAccount(@PathVariable String accountNumber) {
        return accountingFacade.getAccount(accountNumber);
    }

    @PostMapping
    public AccountingApi.AccountStoreResponse newAccount(@RequestBody AccountingApi.AccountCreateRequest account) {
        String accountNumber = accountingFacade.createAccount(account.number(), account.name(), account.bankId(), account.currency(), account.owner(), account.aliases());
        return new AccountingApi.AccountStoreResponse(accountNumber);
    }

    @PutMapping("{accountNumber}")
    public AccountingApi.AccountStoreResponse modifyAccount(@PathVariable String accountNumber, @RequestBody AccountingApi.AccountModifyRequest account) {
        accountingFacade.modifyAccount(accountNumber, account.name(), account.bankId(), account.currency(), account.aliases());
        return new AccountingApi.AccountStoreResponse(accountNumber);
    }

    @GetMapping("{accountNumber}/operations")
    public List<AccountingApi.AccountOperation> listOperations(@PathVariable String accountNumber,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws AccountNotFoundException {
        return accountingFacade.listOperations(accountNumber, from, to);
    }


    @PostMapping(value = "{accountNumber}/operations", params = {"!batch"})
    public AccountingApi.AccountOperation createSingleOperation(@PathVariable String accountNumber,
                                                                @RequestBody AccountingApi.OperationCreateRequest operation)
            throws CategoryNotFoundException {
        return createOperation(accountNumber, operation);
    }

    @PostMapping(value = "{accountNumber}/operations", params = {"batch=false"})
    public AccountingApi.AccountOperation createOperation(@PathVariable String accountNumber,
                                                          @RequestBody AccountingApi.OperationCreateRequest operation)
            throws CategoryNotFoundException {
        Validate.notNull(operation.operationType());
        return accountingFacade.createOperation(
                accountNumber,
                operation.date(),
                operation.authorizationDate(),
                operation.transactionReference(),
                operation.operationType().charAt(0),
                operation.amount(),
                operation.currency(),
                operation.description(),
                operation.comment(),
                operation.categoryId()
        );
    }

    @PostMapping(value = "{accountNumber}/operations", params = {"batch=true"})
    public List<AccountingApi.AccountOperation> createOperations(@PathVariable String accountNumber,
                                                                 @RequestBody AccountingApi.OperationCreateRequest[] operations)
            throws CategoryNotFoundException {
        Arrays.stream(operations)
                .map(AccountingApi.OperationCreateRequest::operationType)
                .forEach(Validate::notNull);

        return accountingFacade.createOperations(
                Arrays.stream(operations).map(request -> new AccountingApi.AccountOperation(
                                accountNumber,
                                request.date(),
                                request.authorizationDate(),
                                request.transactionReference(),
                                null,
                                request.operationType(),
                                request.amount(),
                                request.currency(),
                                request.description(),
                                request.comment(),
                                request.categoryId()
                        ))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("{accountNumber}/holds")
    public List<AccountingApi.HoldOperation> listHoldOperations(@PathVariable String accountNumber,
                                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return accountingFacade.listHolds(accountNumber, from, to);
    }

}
