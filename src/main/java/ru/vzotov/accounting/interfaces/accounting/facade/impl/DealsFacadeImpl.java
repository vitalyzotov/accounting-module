package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.domain.model.BudgetCategoryRepository;
import ru.vzotov.accounting.domain.model.CardOperationRepository;
import ru.vzotov.accounting.domain.model.Deal;
import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.domain.model.DealRepository;
import ru.vzotov.accounting.domain.model.OperationRepository;
import ru.vzotov.accounting.domain.model.TransactionRepository;
import ru.vzotov.accounting.infrastructure.security.SecurityUtils;
import ru.vzotov.accounting.interfaces.accounting.facade.DealsFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.CategoryNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealDTOExpansion;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.DealDTOAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.enrichers.CardOperationEnricher;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.enrichers.OperationEnricher;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.enrichers.PurchaseEnricher;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.enrichers.ReceiptEnricher;
import ru.vzotov.accounting.interfaces.common.guards.OwnedGuard;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;
import ru.vzotov.accounting.interfaces.purchases.facade.impl.assembler.PurchaseDTOAssembler;
import ru.vzotov.banking.domain.model.BudgetCategory;
import ru.vzotov.banking.domain.model.BudgetCategoryId;
import ru.vzotov.banking.domain.model.CardOperation;
import ru.vzotov.banking.domain.model.Operation;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.cashreceipt.domain.model.QRCode;
import ru.vzotov.cashreceipt.domain.model.QRCodeRepository;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.cashreceipt.domain.model.ReceiptRepository;
import ru.vzotov.domain.model.Money;
import ru.vzotov.person.domain.model.PersonId;
import ru.vzotov.purchase.domain.model.Purchase;
import ru.vzotov.purchase.domain.model.PurchaseId;
import ru.vzotov.purchases.domain.model.PurchaseRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;

@Service
public class DealsFacadeImpl implements DealsFacade {

    private final DealRepository dealRepository;
    private final OperationRepository operationRepository;
    private final CardOperationRepository cardOperationRepository;
    private final PurchaseRepository purchaseRepository;
    private final BudgetCategoryRepository budgetCategoryRepository;
    private final QRCodeRepository qrCodeRepository;
    private final ReceiptRepository receiptRepository;
    private final TransactionRepository transactionRepository;
    private final OwnedGuard ownedGuard;

    public DealsFacadeImpl(
            DealRepository dealRepository,
            OperationRepository operationRepository,
            CardOperationRepository cardOperationRepository,
            PurchaseRepository purchaseRepository,
            BudgetCategoryRepository budgetCategoryRepository,
            QRCodeRepository qrCodeRepository,
            ReceiptRepository receiptRepository,
            TransactionRepository transactionRepository,
            OwnedGuard ownedGuard) {
        this.dealRepository = dealRepository;
        this.operationRepository = operationRepository;
        this.cardOperationRepository = cardOperationRepository;
        this.purchaseRepository = purchaseRepository;
        this.budgetCategoryRepository = budgetCategoryRepository;
        this.qrCodeRepository = qrCodeRepository;
        this.receiptRepository = receiptRepository;
        this.transactionRepository = transactionRepository;
        this.ownedGuard = ownedGuard;
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public DealDTO createDeal(LocalDate date, long amount, String currency, String description, String comment,
                              Long categoryId, Collection<String> receipts, Collection<String> operations, Collection<String> purchases
    ) throws CategoryNotFoundException {
        Validate.notNull(receipts);
        Validate.notNull(operations);
        Validate.notNull(purchases);

        final PersonId currentPerson = SecurityUtils.getCurrentPerson();
        Validate.notNull(currentPerson, "Unable to find owner");

        final BudgetCategoryId budgetCategoryId = new BudgetCategoryId(categoryId);
        final BudgetCategory category = categoryId == null ? null :
                budgetCategoryRepository.find(budgetCategoryId);
        if (categoryId != null && category == null) throw new CategoryNotFoundException();

        Deal deal = new Deal(
                DealId.nextId(),
                currentPerson,
                date,
                Money.ofRaw(amount, Currency.getInstance(currency)),
                description, comment, budgetCategoryId,
                receipts.stream().map(ReceiptId::new).collect(Collectors.toSet()),
                operations.stream().map(OperationId::new).collect(Collectors.toSet()),
                Collections.emptySet(),
                purchases.stream().map(PurchaseId::new).collect(Collectors.toList())
        );

        dealRepository.store(deal);
        return DealDTOAssembler.toDTO(deal);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public DealDTO modifyDeal(String dealId, LocalDate date, long amount, String currency,
                              String description, String comment, Long categoryId,
                              Collection<String> receipts, Collection<String> operations, Collection<String> purchases
    ) throws DealNotFoundException, CategoryNotFoundException {
        final Deal deal = dealRepository.find(new DealId(dealId));
        if (deal == null) throw new DealNotFoundException();
        ownedGuard.accessing(deal);

        final BudgetCategoryId budgetCategoryId = new BudgetCategoryId(categoryId);
        final BudgetCategory category = categoryId == null ? null :
                budgetCategoryRepository.find(budgetCategoryId);
        if (categoryId != null && category == null) throw new CategoryNotFoundException();

        deal.setDate(date);
        deal.setAmount(Money.ofRaw(amount, Currency.getInstance(currency)));
        deal.setDescription(description);
        deal.setComment(comment);
        deal.assignCategory(budgetCategoryId);
        deal.setReceipts(receipts.stream().map(ReceiptId::new).collect(Collectors.toSet()));
        deal.setOperations(operations.stream().map(OperationId::new).collect(Collectors.toSet()));
        deal.setPurchases(purchases.stream().map(PurchaseId::new).collect(Collectors.toList()));

        dealRepository.store(deal);
        return DealDTOAssembler.toDTO(deal);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public DealDTO deleteDeal(String dealId, boolean deleteReceipts) throws DealNotFoundException {
        final Deal deal = dealRepository.find(new DealId(dealId));
        if (deal == null) throw new DealNotFoundException();
        ownedGuard.accessing(deal);

        if (deleteReceipts) {
            for (ReceiptId receiptId : deal.receipts()) {
                receiptRepository.delete(receiptId);
                qrCodeRepository.delete(receiptId);
            }
        }
        dealRepository.delete(deal);
        return DealDTOAssembler.toDTO(deal);
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public List<DealDTO> splitDeal(String dealId) throws DealNotFoundException {
        final Deal deal = dealRepository.find(new DealId(dealId));
        if (deal == null) throw new DealNotFoundException();
        ownedGuard.accessing(deal);
        final List<Deal> receiptsResult = new ArrayList<>();
        final List<Deal> operationsResult = new ArrayList<>();

        // find and remove related transactions
        deal.operations().stream()
                .map(transactionRepository::find)
                .filter(Objects::nonNull)
                .distinct()
                .forEach(transactionRepository::delete);

        // create deals for receipts
        final Set<ReceiptId> receipts = new HashSet<>(deal.receipts());
        for (ReceiptId receiptId : receipts) {
            final QRCode qr = qrCodeRepository.find(receiptId);
            final Deal d = new Deal(
                    DealId.nextId(),
                    deal.owner(),
                    qr.code().dateTime().value().toLocalDate(),
                    qr.code().totalSum().negate(),
                    qr.code().fiscalSign().toString(),
                    null,
                    deal.category(),
                    Collections.emptySet(),
                    Collections.emptySet(),
                    Collections.emptySet(),
                    Collections.emptyList()
            );
            deal.moveReceipt(receiptId, d);
            receiptsResult.add(d);
        }

        // create deals for operations
        final Set<OperationId> operations = new HashSet<>(deal.operations());
        for (OperationId operationId : operations) {
            final Operation operation = operationRepository.find(operationId);
            final Deal d = new Deal(
                    DealId.nextId(),
                    deal.owner(),
                    operation.date(),
                    OperationType.WITHDRAW.equals(operation.type()) ? operation.amount() : operation.amount().negate(),
                    operation.description(),
                    operation.comment(),
                    deal.category(),
                    Collections.emptySet(),
                    Collections.emptySet(),
                    Collections.emptySet(),
                    Collections.emptyList()
            );
            deal.moveOperation(operationId, d);
            operationsResult.add(d);
        }

        // move purchases to the most appropriate deal
        final Deal purchaseTarget = receiptsResult.stream()
                .filter(d -> d.amount().rawAmount() < 0)
                .min(comparing(Deal::amount))
                .orElseGet(() -> operationsResult.stream()
                        .filter(d -> d.amount().rawAmount() < 0)
                        .min(comparing(Deal::amount))
                        .orElseGet(() -> Stream.concat(operationsResult.stream(), receiptsResult.stream())
                                .findAny()
                                .orElseThrow(IllegalArgumentException::new)
                        ));
        final List<PurchaseId> purchases = new ArrayList<>(deal.purchases());
        for (PurchaseId purchaseId : purchases) {
            deal.movePurchase(purchaseId, purchaseTarget);
        }

        dealRepository.delete(deal);

        return Stream.concat(receiptsResult.stream(), operationsResult.stream())
                .peek(dealRepository::store)
                .map(DealDTOAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<DealDTO> listDeals(String query, LocalDate from, LocalDate to, Set<DealDTOExpansion> expand) {
        final Collection<PersonId> owners = SecurityUtils.getAuthorizedPersons();

        final Supplier<Stream<Deal>> dealsSupplier = StringUtils.isBlank(query) ?
                () -> dealRepository.findByDate(owners, from, to).stream() :
                () -> dealRepository.findByDate(owners, query, from, to).stream();

        return expandDeals(owners, expand, from, to, dealsSupplier).collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public DealDTO getDeal(String dealId, Set<DealDTOExpansion> expand) throws DealNotFoundException {
        final Deal deal = dealRepository.find(new DealId(dealId));
        if (deal == null) throw new DealNotFoundException();
        ownedGuard.accessing(deal);
        return expandDeals(Collections.emptySet(), expand, null, null, () -> Stream.of(deal))
                .findFirst()
                .orElseThrow(DealNotFoundException::new);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public LocalDate getMinDealDate() {
        return dealRepository.findMinDealDate(SecurityUtils.getAuthorizedPersons());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public LocalDate getMaxDealDate() {
        return dealRepository.findMaxDealDate(SecurityUtils.getAuthorizedPersons());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public LocalDate[] getMinMaxDealDates() {
        return dealRepository.findMinMaxDealDates(SecurityUtils.getAuthorizedPersons());
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public DealDTO mergeDeals(List<String> dealIds) {
        Validate.notNull(dealIds);
        Validate.isTrue(dealIds.size() >= 2);

        final LinkedList<Deal> deals = dealIds.stream()
                .map(DealId::new)
                .map(this::findDealSecurely)
                .collect(Collectors.toCollection(LinkedList::new));

        final Deal earliestDeal = deals.stream().min(comparing(Deal::date))
                .orElseThrow(NullPointerException::new);

        final Deal target = deals.removeFirst();

        final Deal firstOperationDeal = Stream.concat(Stream.of(target), deals.stream())
                .filter(deal -> !deal.operations().isEmpty())
                .min(comparing(Deal::date))
                .orElseThrow(NullPointerException::new);

        deals.forEach(target::join);
        deals.forEach(dealRepository::store);

        Stream.concat(Stream.of(target), deals.stream())
                .map(Deal::comment)
                .filter(StringUtils::isNotBlank)
                .findFirst()
                .ifPresent(target::setComment);

        Stream.concat(Stream.of(target), deals.stream())
                .map(Deal::category)
                .filter(Objects::nonNull)
                .findFirst()
                .ifPresent(target::assignCategory);

        target.setDescription(firstOperationDeal.description());
        target.setDate(earliestDeal.date());

        Money amount = target.operations().stream()
                .map(operationRepository::find)
                .map(op -> switch (op.type()) {
                    case DEPOSIT -> op.amount();
                    case WITHDRAW -> op.amount().negate();
                    default -> throw new IllegalArgumentException();
                })
                .reduce(Money::add).orElseThrow(IllegalArgumentException::new);
        target.setAmount(amount);

        deals.forEach(dealRepository::delete);
        dealRepository.store(target);

        return DealDTOAssembler.toDTO(target);
    }

    private Deal findDealSecurely(DealId dealId) {
        return ownedGuard.accessing(dealRepository.find(dealId));
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<PurchaseDTO> listDealPurchases(String dealId) {
        final Deal deal = findDealSecurely(new DealId(dealId));
        final PurchaseDTOAssembler assembler = new PurchaseDTOAssembler();
        return deal.purchases().stream()
                .map(purchaseRepository::find)
                .map(assembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public void movePurchase(PurchaseId purchaseId, DealId sourceId, DealId targetId) {
        Validate.notNull(purchaseId);
        Validate.notNull(sourceId);
        Validate.notNull(targetId);
        final Deal source = findDealSecurely(sourceId);
        Validate.notNull(source);
        final Deal target = findDealSecurely(targetId);
        Validate.notNull(target);

        source.movePurchase(purchaseId, target);
        dealRepository.store(source);
        dealRepository.store(target);
    }

    private Stream<DealDTO> expandDeals(Collection<PersonId> owners, Set<DealDTOExpansion> expand,
                                        LocalDate cacheFrom, LocalDate cacheTo, Supplier<Stream<Deal>> request) {
        Validate.notNull(expand);
        final boolean expandReceipts = expand.contains(DealDTOExpansion.RECEIPTS);
        final boolean expandOperations = expand.contains(DealDTOExpansion.OPERATIONS);
        final boolean expandCardOperations = expand.contains(DealDTOExpansion.CARD_OPERATIONS);
        final boolean expandPurchases = expand.contains(DealDTOExpansion.PURCHASES);
        final boolean cache = cacheFrom != null && cacheTo != null;

        final List<Operation> operations = cache && expandOperations ?
                operationRepository.findByDate(owners, cacheFrom, cacheTo) : emptyList();
        final List<CardOperation> cardOperations = cache && expandCardOperations ?
                cardOperationRepository.findByDate(cacheFrom, cacheTo) : emptyList();
        final List<QRCode> receipts = cache && expandReceipts ?
                qrCodeRepository.findByDate(owners, cacheFrom, cacheTo) : emptyList();
        final List<Purchase> purchases = cache && expandPurchases ?
                purchaseRepository.findByDate(owners, cacheFrom.atStartOfDay(), cacheTo.plusDays(1).atStartOfDay()) : emptyList();

        final OperationEnricher operationEnricher = new OperationEnricher(operationRepository, operations);
        final CardOperationEnricher cardOperationEnricher = new CardOperationEnricher(cardOperationRepository, cardOperations);
        final ReceiptEnricher receiptEnricher = new ReceiptEnricher(qrCodeRepository, receipts);
        final PurchaseEnricher purchaseEnricher = new PurchaseEnricher(purchaseRepository, purchases);

        Stream<Deal> stream = request.get();

        return stream.map(deal -> DealDTOAssembler.toDTO(
                deal,
                expandReceipts ? (d) -> receiptEnricher.list(DealDTOAssembler.receipts(d)) : null,
                expandOperations ? (d) -> operationEnricher.list(DealDTOAssembler.operations(d)) : null,
                expandCardOperations ? (d) -> cardOperationEnricher.list(DealDTOAssembler.cardOperations(d)) : null,
                expandPurchases ? (d) -> purchaseEnricher.list(DealDTOAssembler.purchases(d)) : null
        ));
    }
}
