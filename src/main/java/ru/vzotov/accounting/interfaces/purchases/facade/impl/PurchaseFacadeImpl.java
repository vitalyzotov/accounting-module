package ru.vzotov.accounting.interfaces.purchases.facade.impl;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.domain.model.Deal;
import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.domain.model.DealRepository;
import ru.vzotov.accounting.interfaces.purchases.facade.PurchasesFacade;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;
import ru.vzotov.accounting.interfaces.purchases.facade.impl.assembler.PurchaseDTOAssembler;
import ru.vzotov.cashreceipt.domain.model.Check;
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryRepository;
import ru.vzotov.cashreceipt.domain.model.ReceiptRepository;
import ru.vzotov.domain.model.Money;
import ru.vzotov.purchase.domain.model.Purchase;
import ru.vzotov.purchase.domain.model.PurchaseId;
import ru.vzotov.purchases.domain.model.PurchaseRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseFacadeImpl implements PurchasesFacade {

    private static final Logger log = LoggerFactory.getLogger(PurchasesFacade.class);

    private final DealRepository dealRepository;

    private final PurchaseRepository purchaseRepository;

    private final PurchaseCategoryRepository categoryRepository;

    private final ReceiptRepository receiptRepository;

    public PurchaseFacadeImpl(DealRepository dealRepository,
                              PurchaseRepository purchaseRepository,
                              PurchaseCategoryRepository categoryRepository,
                              ReceiptRepository receiptRepository) {
        this.dealRepository = dealRepository;
        this.purchaseRepository = purchaseRepository;
        this.categoryRepository = categoryRepository;
        this.receiptRepository = receiptRepository;
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public PurchaseDTO getPurchaseById(String purchaseId) {
        return new PurchaseDTOAssembler().toDTO(purchaseRepository.find(new PurchaseId(purchaseId)));
    }

    @Override
    @Transactional(value = "accounting-tx")
    public PurchaseId deletePurchaseById(String purchaseId) {
        final PurchaseId id = new PurchaseId(purchaseId);
        final Deal deal = dealRepository.findByPurchase(id);
        if(deal != null) {
            deal.removePurchase(id);
            dealRepository.store(deal);
        }
        purchaseRepository.delete(id);
        return id;
    }

    @Override
    @Transactional(value = "accounting-tx", readOnly = true)
    public List<PurchaseDTO> findPurchases(LocalDateTime from, LocalDateTime to) {
        return new PurchaseDTOAssembler().toDTOList(purchaseRepository.findByDate(from, to));
    }

    @Override
    @Transactional(value = "accounting-tx")
    public void modifyPurchase(PurchaseDTO purchase) {
        Validate.notNull(purchase);
        Validate.notNull(purchase.getPurchaseId());

        Purchase p = purchaseRepository.find(new PurchaseId(purchase.getPurchaseId()));
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
    public PurchaseId createPurchase(PurchaseDTO purchase, DealId dealId) {
        Validate.notNull(purchase);
        Validate.notNull(dealId);
        log.info("New purchase {} will be created for deal {}", purchase.getPurchaseId(), dealId);
        final Deal deal = dealRepository.find(dealId);
        Validate.notNull(deal);

        final Purchase p;
        if (purchase.getPurchaseId() != null) {
            throw new IllegalArgumentException("It is not allowed to modify purchase here");
        } else {
            Money price = Money.ofRaw(purchase.getPrice().getAmount(), Currency.getInstance(purchase.getPrice().getCurrency()));
            p = new Purchase(PurchaseId.nextId(), purchase.getName(), purchase.getDateTime(), price, BigDecimal.valueOf(purchase.getQuantity()));
        }

        if (purchase.getCheckId() != null) {
            p.assignCheck(new CheckId(purchase.getCheckId()));
        }
        if (purchase.getCategoryId() != null) {
            p.assignCategory(categoryRepository.findById(new PurchaseCategoryId(purchase.getCategoryId())));
        }

        purchaseRepository.store(p);
        deal.addPurchase(p.purchaseId());
        dealRepository.store(deal);

        return p.purchaseId();
    }

    @Override
    @Transactional(value = "accounting-tx")
    public List<PurchaseDTO> createPurchasesFromReceipt(String receiptId) {
        Validate.notNull(receiptId);
        final CheckId rid = new CheckId(receiptId);
        final Check receipt = receiptRepository.find(rid);
        final Deal deal = dealRepository.findByReceipt(rid);
        final PurchaseDTOAssembler assembler = new PurchaseDTOAssembler();
        final List<PurchaseDTO> result = receipt.products().items().stream()
                .map(i -> {
                    final PurchaseId pid = PurchaseId.nextId();
                    final Purchase p = new Purchase(pid, i.name(), receipt.dateTime(), i.price(), BigDecimal.valueOf(i.quantity()));
                    p.assignCheck(rid);
                    purchaseRepository.store(p);
                    deal.addPurchase(pid);
                    return assembler.toDTO(p);
                })
                .collect(Collectors.toList());
        dealRepository.store(deal);
        return result;
    }
}
