package ru.vzotov.accounting.interfaces.purchases.facade.impl;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.domain.model.Deal;
import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.domain.model.DealRepository;
import ru.vzotov.accounting.infrastructure.security.SecurityUtils;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealNotFoundException;
import ru.vzotov.accounting.interfaces.common.guards.OwnedGuard;
import ru.vzotov.accounting.interfaces.purchases.facade.PurchasesFacade;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;
import ru.vzotov.accounting.interfaces.purchases.facade.impl.assembler.PurchaseDTOAssembler;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryRepository;
import ru.vzotov.cashreceipt.domain.model.Receipt;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.cashreceipt.domain.model.ReceiptRepository;
import ru.vzotov.domain.model.Money;
import ru.vzotov.purchase.domain.model.Purchase;
import ru.vzotov.purchase.domain.model.PurchaseId;
import ru.vzotov.purchases.domain.model.PurchaseRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PurchaseFacadeImpl implements PurchasesFacade {

    private static final Logger log = LoggerFactory.getLogger(PurchasesFacade.class);

    private final DealRepository dealRepository;

    private final PurchaseRepository purchaseRepository;

    private final PurchaseCategoryRepository categoryRepository;

    private final ReceiptRepository receiptRepository;

    private final OwnedGuard ownedGuard;

    public PurchaseFacadeImpl(DealRepository dealRepository,
                              PurchaseRepository purchaseRepository,
                              PurchaseCategoryRepository categoryRepository,
                              ReceiptRepository receiptRepository,
                              OwnedGuard ownedGuard) {
        this.dealRepository = dealRepository;
        this.purchaseRepository = purchaseRepository;
        this.categoryRepository = categoryRepository;
        this.receiptRepository = receiptRepository;
        this.ownedGuard = ownedGuard;
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public PurchaseDTO getPurchaseById(String purchaseId) {
        return new PurchaseDTOAssembler().toDTO(
                ownedGuard.accessing(purchaseRepository.find(new PurchaseId(purchaseId)))
        );
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public PurchaseId deletePurchaseById(String purchaseId) {
        final PurchaseId id = new PurchaseId(purchaseId);
        final Deal deal = ownedGuard.accessing(dealRepository.findByPurchase(id));
        if (deal != null) {
            deal.removePurchase(id);
            dealRepository.store(deal);
        }
        purchaseRepository.delete(id);
        return id;
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    @Secured({"ROLE_USER"})
    public List<PurchaseDTO> findPurchases(LocalDateTime from, LocalDateTime to) {
        return new PurchaseDTOAssembler().toDTOList(purchaseRepository.findByDate(SecurityUtils.getAuthorizedPersons(), from, to));
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public void modifyPurchase(PurchaseDTO purchase) {
        Validate.notNull(purchase);
        Validate.notNull(purchase.getPurchaseId());

        Purchase p = ownedGuard.accessing(purchaseRepository.find(new PurchaseId(purchase.getPurchaseId())));
        Validate.notNull(p);

        if (purchase.getName() != null) {
            p.setName(purchase.getName());
        }

        if (purchase.getDateTime() != null) {
            p.setDateTime(purchase.getDateTime());
        }

        if (purchase.getPrice() != null) {
            Money price = Money.ofRaw(purchase.getPrice().getAmount(), Currency.getInstance(purchase.getPrice().getCurrency()));
            p.setPrice(price);
        }

        if (purchase.getQuantity() != null) {
            p.setQuantity(BigDecimal.valueOf(purchase.getQuantity()));
        }

        if (purchase.getCategoryId() != null) {
            p.assignCategory(categoryRepository.findById(new PurchaseCategoryId(purchase.getCategoryId())));
        }
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public List<PurchaseId> createPurchase(Collection<PurchaseDTO> purchases, DealId dealId) {
        Validate.notEmpty(purchases);
        Validate.notNull(dealId);

        log.info("New purchase will be created for deal {}", dealId);
        final Deal deal = ownedGuard.accessing(dealRepository.find(dealId));
        Validate.notNull(deal);

        final List<Purchase> entities = purchases.stream()
                .map(dto -> {
                    final Purchase p;
                    if (dto.getPurchaseId() != null) {
                        throw new IllegalArgumentException("It is not allowed to modify purchase here");
                    } else {
                        Money price = Money.ofRaw(dto.getPrice().getAmount(), Currency.getInstance(dto.getPrice().getCurrency()));
                        p = new Purchase(PurchaseId.nextId(), SecurityUtils.getCurrentPerson(), dto.getName(), dto.getDateTime(), price, BigDecimal.valueOf(dto.getQuantity()));
                    }

                    if (dto.getReceiptId() != null) {
                        p.assignReceipt(new ReceiptId(dto.getReceiptId()));
                    }
                    if (dto.getCategoryId() != null) {
                        p.assignCategory(ownedGuard.accessing(categoryRepository.findById(new PurchaseCategoryId(dto.getCategoryId()))));
                    }
                    return p;
                })
                .collect(Collectors.toList());

        final List<PurchaseId> result = entities.stream().peek(p -> {
                    purchaseRepository.store(p);
                    deal.addPurchase(p.purchaseId());
                })
                .map(Purchase::purchaseId)
                .collect(Collectors.toList());

        dealRepository.store(deal);

        return result;
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public List<PurchaseDTO> createPurchasesFromReceipt(String receiptId) {
        Validate.notNull(receiptId);
        final ReceiptId rid = new ReceiptId(receiptId);
        final Receipt receipt = ownedGuard.accessing(receiptRepository.find(rid));

        final Deal deal = ownedGuard.accessing(dealRepository.findByReceipt(rid));
        final PurchaseDTOAssembler assembler = new PurchaseDTOAssembler();
        final List<PurchaseDTO> result = receipt.products().items().stream()
                .map(i -> {
                    final PurchaseId pid = PurchaseId.nextId();
                    final Purchase p = new Purchase(pid, deal.owner(), i.name(), receipt.dateTime(), i.price(), BigDecimal.valueOf(i.quantity()));
                    p.assignReceipt(rid);
                    purchaseRepository.store(p);
                    deal.addPurchase(pid);
                    return assembler.toDTO(p);
                })
                .collect(Collectors.toList());
        dealRepository.store(deal);
        return result;
    }

    @Override
    @Transactional(value = "accounting-tx")
    @Secured({"ROLE_USER"})
    public List<PurchaseDTO> createPurchasesFromDealReceipts(String dealId) throws DealNotFoundException {
        Validate.notNull(dealId);
        final Deal deal = ownedGuard.accessing(dealRepository.find(new DealId(dealId)));
        if (deal == null) {
            throw new DealNotFoundException(String.format("Deal with ID=%s not found", dealId));
        }

        final PurchaseDTOAssembler assembler = new PurchaseDTOAssembler();

        final List<PurchaseDTO> result = deal.receipts().stream()
                .map(receiptRepository::find)
                .filter(Objects::nonNull)
                .flatMap(receipt -> ownedGuard.accessing(receipt).products().items().stream().map(i -> {
                    final Purchase p = new Purchase(PurchaseId.nextId(), deal.owner(), i.name(),
                            receipt.dateTime(), i.price(), BigDecimal.valueOf(i.quantity()));
                    p.assignReceipt(receipt.receiptId());
                    return p;
                }))
                .peek(purchaseRepository::store)
                .peek(p -> deal.addPurchase(p.purchaseId()))
                .map(assembler::toDTO)
                .collect(Collectors.toList());

        dealRepository.store(deal);
        return result;
    }
}
