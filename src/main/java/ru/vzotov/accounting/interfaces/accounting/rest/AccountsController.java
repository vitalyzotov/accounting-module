package ru.vzotov.accounting.interfaces.accounting.rest;

import org.apache.commons.lang3.Validate;
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
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.Account;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.AccountOperation;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.HoldOperation;
import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.CategoryNotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/accounting/accounts")
@CrossOrigin
public class AccountsController {

    private final AccountingFacade accountingFacade;

    public AccountsController(AccountingFacade accountingFacade) {
        this.accountingFacade = accountingFacade;
    }

    @GetMapping
    public List<Account> listAccounts() {
        return accountingFacade.listAccounts();
    }

    @GetMapping("{accountNumber}")
    public Account getAccount(@PathVariable String accountNumber) {
        return accountingFacade.getAccount(accountNumber);
    }

    @PostMapping
    public Account.Ref newAccount(@RequestBody Account.Create account) {
        String accountNumber = accountingFacade.createAccount(account.number(), account.name(), account.bankId(),
                account.currency(), account.owner(), account.aliases());
        return new Account.Ref(accountNumber);
    }

    @PutMapping("{accountNumber}")
    public Account.Ref modifyAccount(@PathVariable String accountNumber, @RequestBody Account.Modify account) {
        accountingFacade.modifyAccount(accountNumber, account.name(), account.bankId(), account.currency(), account.aliases());
        return new Account.Ref(accountNumber);
    }

    @GetMapping("{accountNumber}/operations")
    public List<AccountOperation> listOperations(@PathVariable String accountNumber,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) throws AccountNotFoundException {
        return accountingFacade.listOperations(accountNumber, from, to);
    }


    @PostMapping(value = "{accountNumber}/operations", params = {"!batch"})
    public AccountOperation createSingleOperation(@PathVariable String accountNumber,
                                                  @RequestBody AccountOperation.Create operation)
            throws CategoryNotFoundException {
        return createOperation(accountNumber, operation);
    }

    @PostMapping(value = "{accountNumber}/operations", params = {"batch=false"})
    public AccountOperation createOperation(@PathVariable String accountNumber,
                                            @RequestBody AccountOperation.Create operation)
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
    public List<AccountOperation> createOperations(@PathVariable String accountNumber,
                                                   @RequestBody AccountOperation.Create[] operations)
            throws CategoryNotFoundException {
        Arrays.stream(operations)
                .map(AccountOperation.Create::operationType)
                .forEach(Validate::notNull);

        return accountingFacade.createOperations(
                Arrays.stream(operations).map(request -> new AccountOperation(
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
                        .toList()
        );
    }

    @GetMapping("{accountNumber}/holds")
    public List<HoldOperation> listHoldOperations(@PathVariable String accountNumber,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return accountingFacade.listHolds(accountNumber, from, to);
    }

}
