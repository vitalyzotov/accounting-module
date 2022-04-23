package ru.vzotov.accounting.interfaces.accounting.facade;

import ru.vzotov.accounting.application.AccountNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.AccountBindingDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.AccountDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.AccountOperationDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BankDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetCategoryDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.CardDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.CategoryNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.HoldOperationDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.MoneyDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.OperationNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.RemainDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.TransactionDTO;
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

    List<AccountDTO> listAccounts();

    AccountDTO getAccount(String number);

    String createAccount(String number, String name, String bankId, String currency, String owner, List<String> aliases);

    void modifyAccount(String number, String name, String bankId, String currency, List<String> aliases);

    //
    // Remains
    //

    List<RemainDTO> listRemains(LocalDate from, LocalDate to);

    List<RemainDTO> listRemains(String accountNumber);

    RemainDTO getRemain(String remainId);

    String createRemain(String accountNumber, LocalDate date, MoneyDTO value);

    void deleteRemain(String remainId);

    //
    // Categories
    //
    List<BudgetCategoryDTO> listCategories();

    BudgetCategoryDTO getCategory(long id);

    BudgetCategoryDTO createCategory(String name, String color, String icon);

    BudgetCategoryDTO modifyCategory(long id, String newName, String color, String icon) throws CategoryNotFoundException;

    void deleteCategory(long id) throws CategoryNotFoundException;

    //
    // Operations
    //

    List<AccountOperationDTO> listOperations(OperationType type, LocalDate from, LocalDate to);

    List<AccountOperationDTO> listOperations(String accountNumber, LocalDate from, LocalDate to) throws AccountNotFoundException;

    AccountOperationDTO getOperation(String operationId) throws OperationNotFoundException, AccountNotFoundException;

    AccountOperationDTO createOperation(String account,
                                        LocalDate date,
                                        LocalDate authorizationDate,
                                        String transactionReference,
                                        char operationType,
                                        double amount,
                                        String currency,
                                        String description,
                                        String comment,
                                        Long categoryId) throws CategoryNotFoundException;

    AccountOperationDTO deleteOperation(String operationId) throws OperationNotFoundException;

    AccountOperationDTO assignCategoryToOperation(String operationId, long categoryId) throws OperationNotFoundException, CategoryNotFoundException;

    AccountOperationDTO modifyComment(String operationId, String comment) throws OperationNotFoundException;

    //
    // Holds
    //

    List<HoldOperationDTO> listHolds(String accountNumber, LocalDate from, LocalDate to);

    List<HoldOperationDTO> listHolds(OperationType type, LocalDate from, LocalDate to);

    HoldOperationDTO getHold(String holdId) throws OperationNotFoundException;

    void deleteHold(String holdId) throws OperationNotFoundException;

    //
    // Banks
    //

    List<BankDTO> listBanks();

    BankDTO getBank(BankId bankId);

    BankId createBank(BankId bankId, String name, String shortName, String longName);

    BankId modifyBank(BankId bankId, String name, String shortName, String longName);

    //
    // Cards
    //

    List<CardDTO> listCards(BankId issuer);

    CardDTO getCard(CardNumber cardNumber);

    CardNumber createCard(CardNumber number, PersonId holder, YearMonth validThru, BankId issuer, Collection<AccountBindingDTO> accounts);

    CardNumber modifyCard(CardNumber number, PersonId holder, YearMonth validThru, BankId issuer, Collection<AccountBindingDTO> accounts);

    CardNumber deleteCard(CardNumber number);

    //
    // Transactions
    //

    List<TransactionDTO> listTransactions(LocalDate from, LocalDate to, Integer threshold);

    TransactionDTO makeTransaction(OperationId primary, OperationId secondary);

}
