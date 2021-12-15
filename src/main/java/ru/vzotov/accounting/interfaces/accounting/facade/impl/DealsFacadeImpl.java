package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.domain.model.BudgetCategoryRepository;
import ru.vzotov.accounting.domain.model.Deal;
import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.domain.model.DealRepository;
import ru.vzotov.accounting.domain.model.OperationRepository;
import ru.vzotov.accounting.interfaces.accounting.facade.DealsFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.CategoryNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.DealDTOAssembler;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.enrichers.OperationEnricher;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.enrichers.PurchaseEnricher;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.enrichers.ReceiptEnricher;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;
import ru.vzotov.accounting.interfaces.purchases.facade.impl.assembler.PurchaseDTOAssembler;
import ru.vzotov.banking.domain.model.BudgetCategory;
import ru.vzotov.banking.domain.model.BudgetCategoryId;
import ru.vzotov.banking.domain.model.Operation;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.CheckQRCode;
import ru.vzotov.cashreceipt.domain.model.QRCodeRepository;
import ru.vzotov.domain.model.Money;
import ru.vzotov.purchase.domain.model.Purchase;
import ru.vzotov.purchase.domain.model.PurchaseId;
import ru.vzotov.purchases.domain.model.PurchaseRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

@Service
public class DealsFacadeImpl implements DealsFacade {

    private final DealRepository dealRepository;
    private final OperationRepository operationRepository;
    private final PurchaseRepository purchaseRepository;
    private final BudgetCategoryRepository budgetCategoryRepository;
    private final QRCodeRepository qrCodeRepository;

    public DealsFacadeImpl(
            DealRepository dealRepository,
            OperationRepository operationRepository,
            PurchaseRepository purchaseRepository,
            BudgetCategoryRepository budgetCategoryRepository,
            QRCodeRepository qrCodeRepository) {
        this.dealRepository = dealRepository;
        this.operationRepository = operationRepository;
        this.purchaseRepository = purchaseRepository;
        this.budgetCategoryRepository = budgetCategoryRepository;
        this.qrCodeRepository = qrCodeRepository;
    }

    @Override
    @Transactional(value = "accounting-tx")
    public DealDTO createDeal(LocalDate date, long amount, String currency, String description, String comment,
                              Long categoryId, Collection<String> receipts, Collection<String> operations, Collection<String> purchases
    ) throws CategoryNotFoundException {
        Validate.notNull(receipts);
        Validate.notNull(operations);
        Validate.notNull(purchases);

        final BudgetCategoryId budgetCategoryId = new BudgetCategoryId(categoryId);
        final BudgetCategory category = categoryId == null ? null :
                budgetCategoryRepository.find(budgetCategoryId);
        if (categoryId != null && category == null) throw new CategoryNotFoundException();

        Deal deal = new Deal(DealId.nextId(), date,
                Money.ofRaw(amount, Currency.getInstance(currency)),
                description, comment, budgetCategoryId,
                receipts.stream().map(CheckId::new).collect(Collectors.toSet()),
                operations.stream().map(OperationId::new).collect(Collectors.toSet()),
                purchases.stream().map(PurchaseId::new).collect(Collectors.toList())
        );

        dealRepository.store(deal);
        return DealDTOAssembler.toDTO(deal);
    }

    @Override
    @Transactional(value = "accounting-tx")
    public DealDTO modifyDeal(String dealId, LocalDate date, long amount, String currency,
                              String description, String comment, Long categoryId,
                              Collection<String> receipts, Collection<String> operations, Collection<String> purchases
    ) throws DealNotFoundException, CategoryNotFoundException {
        final Deal deal = dealRepository.find(new DealId(dealId));
        if (deal == null) throw new DealNotFoundException();

        final BudgetCategoryId budgetCategoryId = new BudgetCategoryId(categoryId);
        final BudgetCategory category = categoryId == null ? null :
                budgetCategoryRepository.find(budgetCategoryId);
        if (categoryId != null && category == null) throw new CategoryNotFoundException();

        deal.setDate(date);
        deal.setAmount(Money.ofRaw(amount, Currency.getInstance(currency)));
        deal.setDescription(description);
        deal.setComment(comment);
        deal.assignCategory(budgetCategoryId);
        deal.setReceipts(receipts.stream().map(CheckId::new).collect(Collectors.toSet()));
        deal.setOperations(operations.stream().map(OperationId::new).collect(Collectors.toSet()));
        deal.setPurchases(purchases.stream().map(PurchaseId::new).collect(Collectors.toList()));

        dealRepository.store(deal);
        return DealDTOAssembler.toDTO(deal);
    }

    @Override
    @Transactional(value = "accounting-tx")
    public DealDTO deleteDeal(String dealId) throws DealNotFoundException {
        final Deal deal = dealRepository.find(new DealId(dealId));
        if (deal == null) throw new DealNotFoundException();
        dealRepository.delete(deal);
        return DealDTOAssembler.toDTO(deal);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public List<DealDTO> listDeals(LocalDate from, LocalDate to, Set<String> expand) {
        Validate.notNull(expand);
        final boolean expandReceipts = expand.contains("receipts");
        final boolean expandOperations = expand.contains("operations");
        final boolean expandPurchases = expand.contains("purchases");

        final List<Operation> operations = expandOperations ?
                operationRepository.findByDate(from, to) : emptyList();
        final List<CheckQRCode> receipts = expandReceipts ?
                qrCodeRepository.findByDate(from, to) : emptyList();
        final List<Purchase> purchases = expandPurchases ?
                purchaseRepository.findByDate(from.atStartOfDay(), to.plusDays(1).atStartOfDay()) : emptyList();

        final OperationEnricher operationEnricher = new OperationEnricher(operationRepository, operations);
        final ReceiptEnricher receiptEnricher = new ReceiptEnricher(qrCodeRepository, receipts);
        final PurchaseEnricher purchaseEnricher = new PurchaseEnricher(purchaseRepository, purchases);

        Stream<DealDTO> stream = dealRepository.findByDate(from, to)
                .stream()
                .map(DealDTOAssembler::toDTO);
        if (expandOperations)
            stream = stream.peek(dto -> dto.setOperations(operationEnricher.list(dto.getOperations())));
        if (expandReceipts)
            stream = stream.peek(dto -> dto.setReceipts(receiptEnricher.list(dto.getReceipts())));
        if (expandPurchases)
            stream = stream.peek(dto -> dto.setPurchases(purchaseEnricher.list(dto.getPurchases())));

        return stream.collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public DealDTO getDeal(String dealId) throws DealNotFoundException {
        final Deal deal = dealRepository.find(new DealId(dealId));
        if (deal == null) throw new DealNotFoundException();
        return DealDTOAssembler.toDTO(deal);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public LocalDate getMinDealDate() {
        return dealRepository.findMinDealDate();
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public LocalDate getMaxDealDate() {
        return dealRepository.findMaxDealDate();
    }

    @Override
    @Transactional(value = "accounting-tx")
    public DealDTO mergeDeals(List<String> dealIds) {
        Validate.notNull(dealIds);
        Validate.isTrue(dealIds.size() >= 2);

        final LinkedList<Deal> deals = dealIds.stream()
                .map(DealId::new)
                .map(dealRepository::find)
                .collect(Collectors.toCollection(LinkedList::new));

        final Deal earliestDeal = deals.stream().min(Comparator.comparing(Deal::date))
                .orElseThrow(NullPointerException::new);

        final Deal target = deals.removeFirst();

        final Deal firstOperationDeal = Stream.concat(Stream.of(target), deals.stream())
                .filter(deal -> !deal.operations().isEmpty())
                .min(Comparator.comparing(Deal::date))
                .orElseThrow(NullPointerException::new);

        deals.forEach(target::join);
        deals.forEach(dealRepository::store);

        target.setDescription(firstOperationDeal.description());
        target.setDate(earliestDeal.date());

        Money amount = target.operations().stream()
                .map(operationRepository::find)
                .map(op -> {
                    switch (op.type()) {
                        case DEPOSIT:
                            return op.amount();
                        case WITHDRAW:
                            return op.amount().negate();
                        default:
                            throw new IllegalArgumentException();
                    }
                })
                .reduce(Money::add).orElseThrow(IllegalArgumentException::new);
        target.setAmount(amount);

        deals.forEach(dealRepository::delete);
        dealRepository.store(target);

        return DealDTOAssembler.toDTO(target);
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public List<PurchaseDTO> listDealPurchases(String dealId) {
        PurchaseDTOAssembler assembler = new PurchaseDTOAssembler();
        Deal deal = dealRepository.find(new DealId(dealId));
        return deal.purchases().stream()
                .map(purchaseRepository::find)
                .map(assembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(value = "accounting-tx")
    public void movePurchase(PurchaseId purchaseId, DealId sourceId, DealId targetId) {
        Validate.notNull(purchaseId);
        Validate.notNull(sourceId);
        Validate.notNull(targetId);
        final Deal source = dealRepository.find(sourceId);
        Validate.notNull(source);
        final Deal target = dealRepository.find(targetId);
        Validate.notNull(target);
        source.movePurchase(purchaseId, target);
        dealRepository.store(source);
        dealRepository.store(target);
    }
}
