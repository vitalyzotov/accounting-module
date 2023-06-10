package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.application.AccountNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.Account;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.AccountBinding;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.AccountOperation;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.Bank;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.BudgetCategory;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.Card;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.HoldOperation;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.Remain;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.Transaction;
import ru.vzotov.accounting.interfaces.common.CommonApi.Money;
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

    List<Account> listAccounts();

    Account getAccount(String number);

    String createAccount(String number, String name, String bankId, String currency, String owner, List<String> aliases);

    void modifyAccount(String number, String name, String bankId, String currency, List<String> aliases);

    //
    // Remains
    //

    /**
     * Finds remains for given accounts
     *
     * @param accounts   list of accounts. Use {@code null} to find remains for all accounts.
     * @param from       start date
     * @param to         end date
     * @param recentOnly whether to find only most recent remain
     * @return remains for given criteria
     */
    List<Remain> listRemains(Collection<String> accounts, LocalDate from, LocalDate to, boolean recentOnly);

    List<Remain> listRemains(LocalDate from, LocalDate to);

    List<Remain> listRemains(String accountNumber);

    Remain getRemain(String remainId);

    String createRemain(String accountNumber, LocalDate date, Money value);

    void deleteRemain(String remainId);

    //
    // Categories
    //
    List<BudgetCategory> listCategories();

    BudgetCategory getCategory(long id);

    BudgetCategory createCategory(String name, String color, String icon);

    BudgetCategory modifyCategory(long id, String newName, String color, String icon) throws CategoryNotFoundException;

    void deleteCategory(long id) throws CategoryNotFoundException;

    //
    // Operations
    //

    List<AccountOperation> listOperations(OperationType type, LocalDate from, LocalDate to);

    List<AccountOperation> listOperations(String accountNumber, LocalDate from, LocalDate to) throws AccountNotFoundException;

    AccountOperation getOperation(String operationId) throws OperationNotFoundException, AccountNotFoundException;

    AccountOperation createOperation(String account,
                                     LocalDate date,
                                     LocalDate authorizationDate,
                                     String transactionReference,
                                     char operationType,
                                     double amount,
                                     String currency,
                                     String description,
                                     String comment,
                                     Long categoryId) throws CategoryNotFoundException;

    List<AccountOperation> createOperations(List<AccountOperation> data) throws CategoryNotFoundException;

    AccountOperation deleteOperation(String operationId) throws OperationNotFoundException;

    AccountOperation assignCategoryToOperation(String operationId, long categoryId) throws OperationNotFoundException, CategoryNotFoundException;

    AccountOperation modifyComment(String operationId, String comment) throws OperationNotFoundException;

    //
    // Holds
    //

    List<HoldOperation> listHolds(String accountNumber, LocalDate from, LocalDate to);

    List<HoldOperation> listHolds(OperationType type, LocalDate from, LocalDate to);

    HoldOperation getHold(String holdId) throws OperationNotFoundException;

    void deleteHold(String holdId) throws OperationNotFoundException;

    //
    // Banks
    //

    List<Bank> listBanks();

    Bank getBank(BankId bankId);

    BankId createBank(BankId bankId, String name, String shortName, String longName);

    BankId modifyBank(BankId bankId, String name, String shortName, String longName);

    //
    // Cards
    //

    List<Card> listCards(BankId issuer);

    Card getCard(CardNumber cardNumber);

    CardNumber createCard(CardNumber number, PersonId holder, YearMonth validThru, BankId issuer, Collection<AccountBinding> accounts);

    CardNumber modifyCard(CardNumber number, PersonId holder, YearMonth validThru, BankId issuer, Collection<AccountBinding> accounts);

    CardNumber deleteCard(CardNumber number);

    //
    // Transactions
    //

    List<Transaction> listTransactions(LocalDate from, LocalDate to, Integer threshold);

    Transaction makeTransaction(OperationId primary, OperationId secondary);

}
