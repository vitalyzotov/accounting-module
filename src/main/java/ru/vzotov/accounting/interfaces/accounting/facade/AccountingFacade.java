package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.application.AccountNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.CategoryNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.OperationNotFoundException;
import ru.vzotov.banking.domain.model.BankId;
import ru.vzotov.banking.domain.model.CardNumber;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.person.domain.model.PersonId;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;

public interface AccountingFacade {

    //
    // Accounts
    //

    List<AccountingApi.Account> listAccounts();

    AccountingApi.Account getAccount(String number);

    String createAccount(String number, String name, String bankId, String currency, String owner, List<String> aliases);

    void modifyAccount(String number, String name, String bankId, String currency, List<String> aliases);

    //
    // Remains
    //

    /**
     * Finds remains for given accounts
     *
     * @param accounts list of accounts. Use {@code null} to find remains for all accounts.
     * @param from start date
     * @param to end date
     * @param recentOnly whether to find only most recent remain
     * @return remains for given criteria
     */
    List<AccountingApi.Remain> listRemains(Collection<String> accounts, LocalDate from, LocalDate to, boolean recentOnly);

    List<AccountingApi.Remain> listRemains(LocalDate from, LocalDate to);

    List<AccountingApi.Remain> listRemains(String accountNumber);

    AccountingApi.Remain getRemain(String remainId);

    String createRemain(String accountNumber, LocalDate date, AccountingApi.Money value);

    void deleteRemain(String remainId);

    //
    // Categories
    //
    List<AccountingApi.BudgetCategory> listCategories();

    AccountingApi.BudgetCategory getCategory(long id);

    AccountingApi.BudgetCategory createCategory(String name, String color, String icon);

    AccountingApi.BudgetCategory modifyCategory(long id, String newName, String color, String icon) throws CategoryNotFoundException;

    void deleteCategory(long id) throws CategoryNotFoundException;

    //
    // Operations
    //

    List<AccountingApi.AccountOperation> listOperations(OperationType type, LocalDate from, LocalDate to);

    List<AccountingApi.AccountOperation> listOperations(String accountNumber, LocalDate from, LocalDate to) throws AccountNotFoundException;

    AccountingApi.AccountOperation getOperation(String operationId) throws OperationNotFoundException, AccountNotFoundException;

    AccountingApi.AccountOperation createOperation(String account,
                                                   LocalDate date,
                                                   LocalDate authorizationDate,
                                                   String transactionReference,
                                                   char operationType,
                                                   double amount,
                                                   String currency,
                                                   String description,
                                                   String comment,
                                                   Long categoryId) throws CategoryNotFoundException;

    List<AccountingApi.AccountOperation> createOperations(List<AccountingApi.AccountOperation> data) throws CategoryNotFoundException;

    AccountingApi.AccountOperation deleteOperation(String operationId) throws OperationNotFoundException;

    AccountingApi.AccountOperation assignCategoryToOperation(String operationId, long categoryId) throws OperationNotFoundException, CategoryNotFoundException;

    AccountingApi.AccountOperation modifyComment(String operationId, String comment) throws OperationNotFoundException;

    //
    // Holds
    //

    List<AccountingApi.HoldOperation> listHolds(String accountNumber, LocalDate from, LocalDate to);

    List<AccountingApi.HoldOperation> listHolds(OperationType type, LocalDate from, LocalDate to);

    AccountingApi.HoldOperation getHold(String holdId) throws OperationNotFoundException;

    void deleteHold(String holdId) throws OperationNotFoundException;

    //
    // Banks
    //

    List<AccountingApi.Bank> listBanks();

    AccountingApi.Bank getBank(BankId bankId);

    BankId createBank(BankId bankId, String name, String shortName, String longName);

    BankId modifyBank(BankId bankId, String name, String shortName, String longName);

    //
    // Cards
    //

    List<AccountingApi.Card> listCards(BankId issuer);

    AccountingApi.Card getCard(CardNumber cardNumber);

    CardNumber createCard(CardNumber number, PersonId holder, YearMonth validThru, BankId issuer, Collection<AccountingApi.AccountBinding> accounts);

    CardNumber modifyCard(CardNumber number, PersonId holder, YearMonth validThru, BankId issuer, Collection<AccountingApi.AccountBinding> accounts);

    CardNumber deleteCard(CardNumber number);

    //
    // Transactions
    //

    List<AccountingApi.Transaction> listTransactions(LocalDate from, LocalDate to, Integer threshold);

    AccountingApi.Transaction makeTransaction(OperationId primary, OperationId secondary);

}
