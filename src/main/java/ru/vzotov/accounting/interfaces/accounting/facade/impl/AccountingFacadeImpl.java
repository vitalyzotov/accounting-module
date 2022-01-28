package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.application.AccountNotFoundException;
import ru.vzotov.accounting.application.AccountingService;
import ru.vzotov.accounting.domain.model.AccountRepository;
import ru.vzotov.accounting.domain.model.BankRepository;
import ru.vzotov.accounting.domain.model.BudgetCategoryRepository;
import ru.vzotov.accounting.domain.model.CardRepository;
import ru.vzotov.accounting.domain.model.HoldOperationRepository;
import ru.vzotov.accounting.domain.model.OperationRepository;
import ru.vzotov.accounting.domain.model.Remain;
import ru.vzotov.accounting.domain.model.RemainId;
import ru.vzotov.accounting.domain.model.RemainRepository;
import ru.vzotov.accounting.domain.model.TransactionRepository;
import ru.vzotov.accounting.infrastructure.SecurityUtils;
import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
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
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.AccountDtoAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.BankDTOAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.BudgetCategoryDTOAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.CardDTOAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.OperationDTOAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.RemainDTOAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.TransactionDTOAssembler;
import ru.vzotov.banking.domain.model.Account;
import ru.vzotov.banking.domain.model.AccountAliases;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.Bank;
import ru.vzotov.banking.domain.model.BankId;
import ru.vzotov.banking.domain.model.BankingEvents.OperationCreatedEvent;
import ru.vzotov.banking.domain.model.BudgetCategory;
import ru.vzotov.banking.domain.model.BudgetCategoryId;
import ru.vzotov.banking.domain.model.Card;
import ru.vzotov.banking.domain.model.CardNumber;
import ru.vzotov.banking.domain.model.HoldId;
import ru.vzotov.banking.domain.model.HoldOperation;
import ru.vzotov.banking.domain.model.Operation;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.banking.domain.model.Transaction;
import ru.vzotov.banking.domain.model.TransactionCreatedEvent;
import ru.vzotov.banking.domain.model.TransactionReference;
import ru.vzotov.domain.model.Money;
import ru.vzotov.person.domain.model.PersonId;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AccountingFacadeImpl implements AccountingFacade {

    @Autowired
    private AccountingService accountingService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RemainRepository remainRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private BudgetCategoryRepository budgetCategoryRepository;

    @Autowired
    private HoldOperationRepository holdOperationRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PostFilter("hasAuthority(filterObject.owner)")
    public List<AccountDTO> listAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(AccountDtoAssembler::assemble)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountDTO getAccount(String number) {
        return AccountDtoAssembler.assemble(findAccountSecurely(new AccountNumber(number)));
    }

    @PostAuthorize("hasAuthority(returnObject.owner().value())")
    private Account findAccountSecurely(AccountNumber number) {
        return accountRepository.find(number);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String createAccount(String number, String name, String bankId, String currency, String owner, List<String> aliases) {
        Account account = new Account(
                new AccountNumber(number),
                name,
                bankId == null ? null : new BankId(bankId),
                currency == null ? null : Currency.getInstance(currency),
                owner == null ? null : new PersonId(owner),
                aliases == null ? null : new AccountAliases(aliases)
        );
        accountRepository.create(account);

        return account.accountNumber().number();
    }

    @Override
    @Transactional(value = "accounting-tx")
    public void modifyAccount(String number, String name, String bankId, String currency, List<String> aliases) {
        Account account = findAccountSecurely(new AccountNumber(number));
        modifyAccountSecurely(account, name, bankId, currency, aliases);
    }

    @PreAuthorize("hasAuthority(account.owner().value())")
    private void modifyAccountSecurely(Account account, String name, String bankId, String currency, List<String> aliases) {
        account.rename(name);
        account.setBankId(new BankId(bankId));
        account.setCurrency(currency == null ? null : Currency.getInstance(currency));
        account.setAliases(aliases == null ? null : new AccountAliases(aliases));
        accountRepository.update(account);
    }


    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public List<RemainDTO> listRemains(LocalDate from, LocalDate to) {
        //fixme: provide remains for own accounts only
        return remainRepository.findByDate(from, to)
                .stream()
                .map(RemainDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public List<RemainDTO> listRemains(String accountNumber) {
        //fixme: provide remains for own accounts only
        return remainRepository.findByAccountNumber(new AccountNumber(accountNumber))
                .stream()
                .map(RemainDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public RemainDTO getRemain(String remainId) {
        //fixme: provide remains for own accounts only
        return RemainDTOAssembler.toDTO(remainRepository.find(new RemainId(remainId)));
    }

    @Override
    @Transactional("accounting-tx")
    public String createRemain(String accountNumber, LocalDate date, MoneyDTO value) {
        //fixme: create remains for own accounts only
        Remain remain = new Remain(
                new AccountNumber(accountNumber),
                date,
                Money.ofRaw(value.getAmount(), Currency.getInstance(value.getCurrency()))
        );
        remainRepository.store(remain);
        return remain.remainId().value();
    }

    @Override
    @Transactional("accounting-tx")
    public void deleteRemain(String remainId) {
        //fixme: provide remains for own accounts only
        Remain remain = remainRepository.find(new RemainId(remainId));
        deleteRemain(remain);
    }

    protected void deleteRemain(Remain remain) {
        remainRepository.delete(remain);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<AccountOperationDTO> listOperations(OperationType type, LocalDate from, LocalDate to) {
        Validate.notNull(from);
        Validate.notNull(to);
        final Collection<PersonId> owners = SecurityUtils.getAuthorizedPersons();
        return (type == null ? operationRepository.findByDate(owners, from, to) : operationRepository.findByTypeAndDate(owners, type, from, to))
                .stream()
                .map(OperationDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<AccountOperationDTO> listOperations(String accountNumber, LocalDate from, LocalDate to) throws AccountNotFoundException {
        final AccountNumber number = new AccountNumber(accountNumber);
        Account account = findAccountSecurely(number);
        if(account == null) {
            throw new AccountNotFoundException();
        }
        return operationRepository.findByAccountAndDate(number, from, to)
                .stream()
                .map(OperationDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountOperationDTO getOperation(String operationId) throws OperationNotFoundException, AccountNotFoundException {
        Operation operation = operationRepository.find(new OperationId(operationId));
        if (operation == null) {
            throw new OperationNotFoundException();
        }
        Account account = findAccountSecurely(operation.account());
        if(account == null) {
            throw new AccountNotFoundException();
        }

        return OperationDTOAssembler.toDTO(operation);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountOperationDTO createOperation(String accountNumberValue, LocalDate date, LocalDate authorizationDate,
                                               String transactionReference, char operationType,
                                               double amount, String currency,
                                               String description, String comment,
                                               Long categoryId) throws CategoryNotFoundException {
        Validate.notNull(accountNumberValue, "Account required");
        Validate.notNull(date, "Date required");
        Validate.notNull(currency, "Currency required");
        Validate.notNull(transactionReference, "Transaction reference required");

        final OperationType type = OperationType.of(operationType);
        final AccountNumber accountNumber = new AccountNumber(accountNumberValue);
        final Account account = findAccountSecurely(accountNumber);
        Validate.notNull(account, "Account not found");

        final Money money = new Money(amount, Currency.getInstance(currency));
        final TransactionReference txRef = new TransactionReference(transactionReference);

        final BudgetCategoryId id = new BudgetCategoryId(categoryId);
        final BudgetCategory category = categoryId == null ? null : budgetCategoryRepository.find(id);
        if (categoryId != null && category == null) {
            throw new CategoryNotFoundException();
        }

        final OperationId operationId = new OperationId(date, type, accountNumber, money, txRef);
        final Operation operation = new Operation(operationId, txRef, authorizationDate, date, money, type, accountNumber, description, id, comment);
        operationRepository.store(operation);
        eventPublisher.publishEvent(new OperationCreatedEvent(operationId));

        return OperationDTOAssembler.toDTO(operation);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountOperationDTO deleteOperation(String operationId) throws OperationNotFoundException {
        Operation operation = operationRepository.find(new OperationId(operationId));
        if (operation == null) {
            throw new OperationNotFoundException();
        }
        final Account account = findAccountSecurely(operation.account());
        Validate.notNull(account, "Account not found");

        operationRepository.delete(operation);
        return OperationDTOAssembler.toDTO(operation);
    }

    @Override
    @Transactional("accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountOperationDTO assignCategoryToOperation(String operationId, long categoryId) throws OperationNotFoundException, CategoryNotFoundException {
        Operation operation = operationRepository.find(new OperationId(operationId));
        if (operation == null) {
            throw new OperationNotFoundException();
        }

        final Account account = findAccountSecurely(operation.account());
        Validate.notNull(account, "Account not found");

        final BudgetCategoryId id = new BudgetCategoryId(categoryId);
        BudgetCategory category = budgetCategoryRepository.find(id);
        if (category == null) {
            throw new CategoryNotFoundException();
        }

        operation.assignCategory(id);

        operationRepository.store(operation);
        return OperationDTOAssembler.toDTO(operation);
    }

    @Override
    @Transactional("accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public AccountOperationDTO modifyComment(String operationId, String comment) throws OperationNotFoundException {
        Operation operation = operationRepository.find(new OperationId(operationId));
        if (operation == null) {
            throw new OperationNotFoundException();
        }

        final Account account = findAccountSecurely(operation.account());
        Validate.notNull(account, "Account not found");

        return modifyOperationComment(comment, operation);
    }

    private AccountOperationDTO modifyOperationComment(String newComment, Operation operation) {
        operation.setComment(newComment);

        operationRepository.store(operation);
        return OperationDTOAssembler.toDTO(operation);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<HoldOperationDTO> listHolds(String accountNumber, LocalDate from, LocalDate to) {
        final Account account = findAccountSecurely(new AccountNumber(accountNumber));
        return holdOperationRepository.findByAccountAndDate(account.accountNumber(), from, to)
                .stream()
                .map(OperationDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<HoldOperationDTO> listHolds(OperationType type, LocalDate from, LocalDate to) {
        Validate.notNull(from);
        Validate.notNull(to);
        return (type == null ? holdOperationRepository.findByDate(SecurityUtils.getAuthorizedPersons(), from, to)
                : holdOperationRepository.findByTypeAndDate(SecurityUtils.getAuthorizedPersons(), type, from, to))
                .stream()
                .map(OperationDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteHold(String holdId) throws OperationNotFoundException {
        final HoldOperation operation = holdOperationRepository.find(new HoldId(holdId));
        if (operation == null) {
            throw new OperationNotFoundException();
        }
        final Account account = findAccountSecurely(operation.account());
        Validate.notNull(account, "Account not found");
        accountingService.deleteHoldOperation(operation);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public HoldOperationDTO getHold(String holdId) throws OperationNotFoundException {
        final HoldOperation operation = holdOperationRepository.find(new HoldId(holdId));
        if (operation == null) {
            throw new OperationNotFoundException();
        }
        final Account account = findAccountSecurely(operation.account());
        Validate.notNull(account, "Account not found");
        return OperationDTOAssembler.toDTO(operation);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<BankDTO> listBanks() {
        return bankRepository.findAll()
                .stream()
                .map(BankDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public BankDTO getBank(BankId bankId) {
        return BankDTOAssembler.toDTO(bankRepository.find(bankId));
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BankId createBank(BankId bankId, String name, String shortName, String longName) {
        bankRepository.store(new Bank(bankId, name, shortName, longName));
        return bankId;
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BankId modifyBank(BankId bankId, String name, String shortName, String longName) {
        Bank bank = bankRepository.find(bankId);
        if (bank == null) return null;
        bank.rename(name, shortName, longName);
        bankRepository.store(bank);
        return bankId;
    }

    @PostFilter("hasAuthority(filterObject.owner().value())")
    private List<Card> filterSecurely(List<Card> cards) {
        return cards;
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<CardDTO> listCards(BankId issuer) {
        final PersonId currentPerson = SecurityUtils.getCurrentPerson();
        return filterSecurely(issuer == null ? cardRepository.find(currentPerson) : cardRepository.findByBank(currentPerson, issuer))
                .stream()
                .map(CardDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public CardDTO getCard(CardNumber cardNumber) {
        return CardDTOAssembler.toDTO(findCardSecurely(cardNumber));
    }

    @PostAuthorize("hasAuthority(returnObject.owner().value())")
    private Card findCardSecurely(CardNumber cardNumber) {
        return cardRepository.find(cardNumber);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CardNumber createCard(CardNumber number, YearMonth validThru, BankId issuer) {
        cardRepository.store(new Card(number, SecurityUtils.getCurrentPerson(), validThru, issuer));
        return number;
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CardNumber deleteCard(CardNumber number) {
        Card card = findCardSecurely(number);
        if (card == null) return null;
        cardRepository.delete(card);
        return number;
    }

    @Override
    @Transactional(value = "accounting-tx")
    public List<TransactionDTO> listTransactions(LocalDate from, LocalDate to, Integer threshold) {
//        final List<Operation> operations = threshold == null ?
//                Collections.emptyList() :
//                operationRepository.findByDate(from, to);
//
//        final List<Transaction> transactions = threshold == null ?
//                Collections.emptyList() :
//                Transaction.matchOperations(operations, threshold, null);

        final List<Transaction> knownTransactions = transactionRepository.findByDate(from, to);
        return knownTransactions.stream()//, transactions.stream())
                //.distinct()
                .map(TransactionDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx")
    public TransactionDTO makeTransaction(OperationId primaryId, OperationId secondaryId) {
        Operation primary = operationRepository.find(primaryId);
        Validate.notNull(primary);
        Operation secondary = operationRepository.find(secondaryId);
        Validate.notNull(secondary);

        Validate.isTrue(primary.amount().equals(secondary.amount()), "Should have same amount");
        //can have same accounts
        //Validate.isTrue(!primary.account().equals(secondary.account()), "Should have different accounts");
        Validate.isTrue(!primary.type().equals(secondary.type()), "Should have different types");

        Transaction transaction = new Transaction(primaryId, secondaryId);
        transactionRepository.store(transaction);
        eventPublisher.publishEvent(new TransactionCreatedEvent(primaryId, secondaryId));

        return TransactionDTOAssembler.toDTO(transaction);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<BudgetCategoryDTO> listCategories() {
        return budgetCategoryRepository.findAll(SecurityUtils.getCurrentPerson())
                .stream()
                .map(BudgetCategoryDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @PreAuthorize("hasRole('ROLE_USER')")
    public BudgetCategoryDTO getCategory(long id) {
        return BudgetCategoryDTOAssembler.toDTO(findBudgetCategorySecurely(id));
    }

    @PostAuthorize("hasAuthority(returnObject.owner().value())")
    private BudgetCategory findBudgetCategorySecurely(long id) {
        return budgetCategoryRepository.find(new BudgetCategoryId(id));
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public BudgetCategoryDTO createCategory(String name, String color, String icon) {
        BudgetCategory category = new BudgetCategory(BudgetCategoryId.of(name), SecurityUtils.getCurrentPerson(),
                name, decodeColor(color), icon);
        budgetCategoryRepository.store(category);
        return BudgetCategoryDTOAssembler.toDTO(category);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public BudgetCategoryDTO modifyCategory(long id, String newName, String color, String icon) throws CategoryNotFoundException {
        final BudgetCategory category = findBudgetCategorySecurely(id);
        if (category == null) {
            throw new CategoryNotFoundException();
        }

        final BudgetCategory other = budgetCategoryRepository.find(category.owner(), newName);
        if (other != null) {
            throw new IllegalArgumentException();
        }

        category.rename(newName);
        category.setColor(decodeColor(color));
        category.setIcon(icon);

        budgetCategoryRepository.store(category);

        return BudgetCategoryDTOAssembler.toDTO(category);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteCategory(long id) throws CategoryNotFoundException {
        BudgetCategory category = findBudgetCategorySecurely(id);
        if (category == null) {
            throw new CategoryNotFoundException();
        }
        budgetCategoryRepository.delete(category);
    }

    private Integer decodeColor(String value) {
        if (value == null) return null;
        return value.charAt(0) == '#' ?
                Integer.parseUnsignedInt(value.substring(1), 16) :
                Integer.parseUnsignedInt(value, 16);
    }
}
