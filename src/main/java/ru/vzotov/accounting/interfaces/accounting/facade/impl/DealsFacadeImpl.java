package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.domain.model.BudgetCategoryRepository;
import ru.vzotov.accounting.domain.model.Deal;
import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.domain.model.DealRepository;
import ru.vzotov.accounting.interfaces.accounting.facade.DealsFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.CategoryNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.DealDTOAssembler;
import ru.vzotov.banking.domain.model.BudgetCategory;
import ru.vzotov.banking.domain.model.BudgetCategoryId;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.domain.model.Money;
import ru.vzotov.purchase.domain.model.PurchaseId;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DealsFacadeImpl implements DealsFacade {

    private final DealRepository dealRepository;
    private final BudgetCategoryRepository budgetCategoryRepository;

    public DealsFacadeImpl(DealRepository dealRepository, BudgetCategoryRepository budgetCategoryRepository) {
        this.dealRepository = dealRepository;
        this.budgetCategoryRepository = budgetCategoryRepository;
    }

    @Override
    @Transactional(value = "accounting-tx")
    public DealDTO createDeal(LocalDate date, long amount, String currency, String description, String comment,
                              Long categoryId, Collection<String> receipts, Collection<String> operations, Collection<String> purchases
    ) throws CategoryNotFoundException {
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
    public List<DealDTO> listDeals(LocalDate from, LocalDate to) {
        return dealRepository.findByDate(from, to)
                .stream()
                .map(DealDTOAssembler::toDTO)
                .collect(Collectors.toList());
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
}
