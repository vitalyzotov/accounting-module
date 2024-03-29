package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.apache.commons.lang3.Validate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.annotation.Secured;
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
import ru.vzotov.accounting.infrastructure.security.SecurityUtils;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.CategoryNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.OperationNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.AccountAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.BankAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.BudgetCategoryAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.CardAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.OperationAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.RemainAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.TransactionAssembler;
import ru.vzotov.accounting.interfaces.common.CommonApi;
import ru.vzotov.accounting.interfaces.common.guards.OwnedGuard;
import ru.vzotov.banking.domain.model.Account;
import ru.vzotov.banking.domain.model.AccountAliases;
import ru.vzotov.banking.domain.model.AccountBinding;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountingFacadeImpl implements AccountingFacade {

    private final AccountingService accountingService;

    private final AccountRepository accountRepository;

    private final RemainRepository remainRepository;

    private final OperationRepository operationRepository;

    private final BudgetCategoryRepository budgetCategoryRepository;

    private final HoldOperationRepository holdOperationRepository;

    private final BankRepository bankRepository;

    private final CardRepository cardRepository;

    private final TransactionRepository transactionRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final OwnedGuard ownedGuard;

    public AccountingFacadeImpl(AccountingService accountingService,
                                AccountRepository accountRepository,
                                RemainRepository remainRepository,
                                OperationRepository operationRepository,
                                BudgetCategoryRepository budgetCategoryRepository,
                                HoldOperationRepository holdOperationRepository,
                                BankRepository bankRepository,
                                CardRepository cardRepository,
                                TransactionRepository transactionRepository,
                                ApplicationEventPublisher eventPublisher,
                                OwnedGuard ownedGuard) {
        this.accountingService = accountingService;
        this.accountRepository = accountRepository;
        this.remainRepository = remainRepository;
        this.operationRepository = operationRepository;
        this.budgetCategoryRepository = budgetCategoryRepository;
        this.holdOperationRepository = holdOperationRepository;
        this.bankRepository = bankRepository;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
        this.eventPublisher = eventPublisher;
        this.ownedGuard = ownedGuard;
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<AccountingApi.Account> listAccounts() {
        return accountRepository.findAll(SecurityUtils.getAuthorizedPersons())
                .stream()
                .map(AccountAssembler::assemble)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public AccountingApi.Account getAccount(String number) {
        return AccountAssembler.assemble(ownedGuard.accessing(
                accountRepository.find(new AccountNumber(number))
        ));
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
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
    @Secured({"ROLE_USER"})
    public void modifyAccount(String number, String name, String bankId, String currency, List<String> aliases) {
        Account account = ownedGuard.accessing(accountRepository.find(new AccountNumber(number)));
        account.rename(name);
        account.setBankId(bankId == null ? null : new BankId(bankId));
        account.setCurrency(currency == null ? null : Currency.getInstance(currency));
        account.setAliases(aliases == null ? null : new AccountAliases(aliases));
        accountRepository.update(account);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<AccountingApi.Remain> listRemains(Collection<String> accounts, LocalDate from, LocalDate to, boolean recentOnly) {
        return remainRepository.find(
                        SecurityUtils.getAuthorizedPersons(),
                        accounts == null ? null : accounts.stream()
                                .map(AccountNumber::new)
                                .peek(number -> ownedGuard.accessing(accountRepository.find(number)))
                                .collect(Collectors.toList()),
                        from, to,
                        recentOnly
                )
                .stream()
                .map(RemainAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<AccountingApi.Remain> listRemains(LocalDate from, LocalDate to) {
        return remainRepository.findByDate(SecurityUtils.getAuthorizedPersons(), from, to)
                .stream()
                .map(RemainAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<AccountingApi.Remain> listRemains(String accountNumber) {
        final AccountNumber account = new AccountNumber(accountNumber);
        ownedGuard.accessing(accountRepository.find(account));
        return remainRepository.findByAccountNumber(account)
                .stream()
                .map(RemainAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public AccountingApi.Remain getRemain(String remainId) {
        final Remain remain = remainRepository.find(new RemainId(remainId));
        ownedGuard.accessing(accountRepository.find(remain.account()));
        return RemainAssembler.toDTO(remain);
    }

    @Override
    @Transactional("accounting-tx")
    @Secured({"ROLE_USER"})
    public String createRemain(String accountNumber, LocalDate date, CommonApi.Money value) {
        final AccountNumber account = new AccountNumber(accountNumber);
        ownedGuard.accessing(accountRepository.find(account));
        Remain remain = new Remain(account, date,
                ru.vzotov.domain.model.Money.ofRaw(value.amount(), Currency.getInstance(value.currency()))
        );
        remainRepository.store(remain);
        return remain.remainId().value();
    }

    @Override
    @Transactional("accounting-tx")
    @Secured({"ROLE_USER"})
    public void deleteRemain(String remainId) {
        Remain remain = remainRepository.find(new RemainId(remainId));
        Validate.notNull(remain, "Remain not found");
        ownedGuard.accessing(accountRepository.find(remain.account()));
        remainRepository.delete(remain);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<AccountingApi.AccountOperation> listOperations(OperationType type, LocalDate from, LocalDate to) {
        Validate.notNull(from);
        Validate.notNull(to);
        final Collection<PersonId> owners = SecurityUtils.getAuthorizedPersons();
        return (type == null ? operationRepository.findByDate(owners, from, to) : operationRepository.findByTypeAndDate(owners, type, from, to))
                .stream()
                .map(OperationAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<AccountingApi.AccountOperation> listOperations(String accountNumber, LocalDate from, LocalDate to) throws AccountNotFoundException {
        final AccountNumber number = new AccountNumber(accountNumber);
        Account account = ownedGuard.accessing(accountRepository.find(number));
        if (account == null) {
            throw new AccountNotFoundException();
        }
        return operationRepository.findByAccountAndDate(number, from, to)
                .stream()
                .map(OperationAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public AccountingApi.AccountOperation getOperation(String operationId) throws OperationNotFoundException, AccountNotFoundException {
        final Operation operation = operationRepository.find(new OperationId(operationId));
        if (operation == null) {
            throw new OperationNotFoundException();
        }
        final Account account = ownedGuard.accessing(accountRepository.find(operation.account()));
        if (account == null) {
            throw new AccountNotFoundException();
        }

        return OperationAssembler.toDTO(operation);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public AccountingApi.AccountOperation createOperation(String accountNumberValue, LocalDate date, LocalDate authorizationDate,
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
        final Account account = ownedGuard.accessing(accountRepository.find(accountNumber));
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

        return OperationAssembler.toDTO(operation);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public List<AccountingApi.AccountOperation> createOperations(List<AccountingApi.AccountOperation> data) throws CategoryNotFoundException {
        Validate.notNull(data, "Data required");

        List<AccountingApi.AccountOperation> result = new ArrayList<>(data.size());
        for (AccountingApi.AccountOperation operation : data) {
            result.add(createOperation(
                    operation.account(),
                    operation.date(),
                    operation.authorizationDate(),
                    operation.transactionReference(),
                    operation.operationType().charAt(0),
                    operation.amount(),
                    operation.currency(),
                    operation.description(),
                    operation.comment(),
                    operation.categoryId()
            ));
        }
        return result;
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public AccountingApi.AccountOperation deleteOperation(String operationId) throws OperationNotFoundException {
        Operation operation = operationRepository.find(new OperationId(operationId));
        if (operation == null) {
            throw new OperationNotFoundException();
        }
        final Account account = ownedGuard.accessing(accountRepository.find(operation.account()));
        Validate.notNull(account, "Account not found");

        operationRepository.delete(operation);
        return OperationAssembler.toDTO(operation);
    }

    @Override
    @Transactional("accounting-tx")
    @Secured({"ROLE_USER"})
    public AccountingApi.AccountOperation assignCategoryToOperation(String operationId, long categoryId) throws OperationNotFoundException, CategoryNotFoundException {
        Operation operation = operationRepository.find(new OperationId(operationId));
        if (operation == null) {
            throw new OperationNotFoundException();
        }

        final Account account = ownedGuard.accessing(accountRepository.find(operation.account()));
        Validate.notNull(account, "Account not found");

        final BudgetCategoryId id = new BudgetCategoryId(categoryId);
        BudgetCategory category = budgetCategoryRepository.find(id);
        if (category == null) {
            throw new CategoryNotFoundException();
        }

        operation.assignCategory(id);

        operationRepository.store(operation);
        return OperationAssembler.toDTO(operation);
    }

    @Override
    @Transactional("accounting-tx")
    @Secured({"ROLE_USER"})
    public AccountingApi.AccountOperation modifyComment(String operationId, String comment) throws OperationNotFoundException {
        Operation operation = operationRepository.find(new OperationId(operationId));
        if (operation == null) {
            throw new OperationNotFoundException();
        }

        final Account account = ownedGuard.accessing(accountRepository.find(operation.account()));
        Validate.notNull(account, "Account not found");

        return modifyOperationComment(comment, operation);
    }

    private AccountingApi.AccountOperation modifyOperationComment(String newComment, Operation operation) {
        operation.setComment(newComment);

        operationRepository.store(operation);
        return OperationAssembler.toDTO(operation);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<AccountingApi.HoldOperation> listHolds(String accountNumber, LocalDate from, LocalDate to) {
        final Account account = ownedGuard.accessing(accountRepository.find(new AccountNumber(accountNumber)));
        return holdOperationRepository.findByAccountAndDate(account.accountNumber(), from, to)
                .stream()
                .map(OperationAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<AccountingApi.HoldOperation> listHolds(OperationType type, LocalDate from, LocalDate to) {
        Validate.notNull(from);
        Validate.notNull(to);
        return (type == null ? holdOperationRepository.findByDate(SecurityUtils.getAuthorizedPersons(), from, to)
                : holdOperationRepository.findByTypeAndDate(SecurityUtils.getAuthorizedPersons(), type, from, to))
                .stream()
                .map(OperationAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public void deleteHold(String holdId) throws OperationNotFoundException {
        final HoldOperation operation = holdOperationRepository.find(new HoldId(holdId));
        if (operation == null) {
            throw new OperationNotFoundException();
        }
        final Account account = ownedGuard.accessing(accountRepository.find(operation.account()));
        Validate.notNull(account, "Account not found");
        accountingService.deleteHoldOperation(operation);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public AccountingApi.HoldOperation getHold(String holdId) throws OperationNotFoundException {
        final HoldOperation operation = holdOperationRepository.find(new HoldId(holdId));
        if (operation == null) {
            throw new OperationNotFoundException();
        }
        final Account account = ownedGuard.accessing(accountRepository.find(operation.account()));
        Validate.notNull(account, "Account not found");
        return OperationAssembler.toDTO(operation);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<AccountingApi.Bank> listBanks() {
        return bankRepository.findAll()
                .stream()
                .map(BankAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public AccountingApi.Bank getBank(BankId bankId) {
        return BankAssembler.toDTO(bankRepository.find(bankId));
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_ADMIN"})
    public BankId createBank(BankId bankId, String name, String shortName, String longName) {
        bankRepository.store(new Bank(bankId, name, shortName, longName));
        return bankId;
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_ADMIN"})
    public BankId modifyBank(BankId bankId, String name, String shortName, String longName) {
        Bank bank = bankRepository.find(bankId);
        if (bank == null) return null;
        bank.rename(name, shortName, longName);
        bankRepository.store(bank);
        return bankId;
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<AccountingApi.Card> listCards(BankId issuer) {
        final PersonId currentPerson = SecurityUtils.getCurrentPerson();
        return ownedGuard.filter(issuer == null ? cardRepository.find(currentPerson) : cardRepository.findByBank(currentPerson, issuer))
                .stream()
                .map(CardAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public AccountingApi.Card getCard(CardNumber cardNumber) {
        return CardAssembler.toDTO(ownedGuard.accessing(cardRepository.find(cardNumber)));
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER') and hasAuthority(#holder.authority())")
    public CardNumber createCard(CardNumber number, PersonId holder, YearMonth validThru, BankId issuer, Collection<AccountingApi.AccountBinding> accounts) {
        final Card card = new Card(number, holder, validThru, issuer);
        Optional.ofNullable(accounts).stream().flatMap(Collection::stream)
                .forEach(b -> card.bindToAccount(
                        new AccountNumber(b.accountNumber()),
                        b.from(),
                        b.to()
                ));
        cardRepository.store(card);
        return number;
    }

    @Override
    @Transactional(value = "accounting-tx")
    @PreAuthorize("hasRole('ROLE_USER') and hasAuthority(#holder.authority())")
    public CardNumber modifyCard(CardNumber number, PersonId holder, YearMonth validThru, BankId issuer, Collection<AccountingApi.AccountBinding> accounts) {
        final Card card = cardRepository.find(number);
        Validate.notNull(card, "Card not found");
        Validate.isTrue(card.owner().equals(holder), "Card holder cannot be modified");
        Validate.isTrue(card.issuer().equals(issuer), "Card issuer cannot be modified");
        card.setValidThru(validThru);
        card.unbindAll();
        accounts.stream()
                .map(d -> new AccountBinding(new AccountNumber(d.accountNumber()), d.from(), d.to()))
                .forEach(b -> card.bindToAccount(b.accountNumber(), b.from(), b.to()));
        cardRepository.store(card);
        return number;
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public CardNumber deleteCard(CardNumber number) {
        final Card card = ownedGuard.accessing(cardRepository.find(number));
        if (card == null) return null;
        cardRepository.delete(card);
        return number;
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public List<AccountingApi.Transaction> listTransactions(LocalDate from, LocalDate to, Integer threshold) {
        final List<Transaction> knownTransactions = transactionRepository.findByDate(SecurityUtils.getAuthorizedPersons(), from, to);
        return knownTransactions.stream()
                .map(TransactionAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public AccountingApi.Transaction makeTransaction(OperationId primaryId, OperationId secondaryId) {
        Operation primary = operationRepository.find(primaryId);
        Validate.notNull(primary);
        Validate.notNull(ownedGuard.accessing(accountRepository.find(primary.account())), "Primary account not found");

        Operation secondary = operationRepository.find(secondaryId);
        Validate.notNull(secondary);
        Validate.notNull(ownedGuard.accessing(accountRepository.find(secondary.account())), "Secondary account not found");

        Validate.isTrue(primary.amount().equals(secondary.amount()), "Should have same amount");
        //can have same accounts
        //Validate.isTrue(!primary.account().equals(secondary.account()), "Should have different accounts");
        Validate.isTrue(!primary.type().equals(secondary.type()), "Should have different types");

        Transaction transaction = new Transaction(primaryId, secondaryId);
        transactionRepository.store(transaction);
        eventPublisher.publishEvent(new TransactionCreatedEvent(primaryId, secondaryId));

        return TransactionAssembler.toDTO(transaction);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<AccountingApi.BudgetCategory> listCategories() {
        return budgetCategoryRepository.findAll(SecurityUtils.getCurrentPerson())
                .stream()
                .map(BudgetCategoryAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public AccountingApi.BudgetCategory getCategory(long id) {
        return BudgetCategoryAssembler.toDTO(
                ownedGuard.accessing(budgetCategoryRepository.find(new BudgetCategoryId(id)))
        );
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public AccountingApi.BudgetCategory createCategory(String name, String color, String icon) {
        BudgetCategory category = new BudgetCategory(BudgetCategoryId.of(name), SecurityUtils.getCurrentPerson(),
                name, decodeColor(color), icon);
        budgetCategoryRepository.store(category);
        return BudgetCategoryAssembler.toDTO(category);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public AccountingApi.BudgetCategory modifyCategory(long id, String newName, String color, String icon) throws CategoryNotFoundException {
        final BudgetCategory category = ownedGuard.accessing(budgetCategoryRepository.find(new BudgetCategoryId(id)));
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

        return BudgetCategoryAssembler.toDTO(category);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public void deleteCategory(long id) throws CategoryNotFoundException {
        BudgetCategory category = ownedGuard.accessing(budgetCategoryRepository.find(new BudgetCategoryId(id)));
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
