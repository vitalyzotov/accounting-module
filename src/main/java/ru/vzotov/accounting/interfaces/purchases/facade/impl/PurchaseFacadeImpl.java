package ru.vzotov.accounting.interfaces.purchases.facade.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.cashreceipt.domain.model.Check;
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.ReceiptRepository;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryRepository;
import ru.vzotov.accounting.interfaces.purchases.facade.PurchasesFacade;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;
import ru.vzotov.accounting.interfaces.purchases.facade.impl.assembler.PurchaseDTOAssembler;
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

    private final PurchaseRepository purchaseRepository;

    private final PurchaseCategoryRepository categoryRepository;

    private final ReceiptRepository receiptRepository;

    public PurchaseFacadeImpl(PurchaseRepository purchaseRepository,
                              PurchaseCategoryRepository categoryRepository,
                              ReceiptRepository receiptRepository) {
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
        PurchaseId id = new PurchaseId(purchaseId);
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
    public PurchaseId storePurchase(PurchaseDTO purchase) {

        Purchase p;

        if (purchase.getPurchaseId() != null) {
            p = purchaseRepository.find(new PurchaseId(purchase.getPurchaseId()));

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

        return p.purchaseId();
    }

    @Override
    @Transactional(value = "accounting-tx")
    public List<PurchaseDTO> createPurchasesFromCheck(String checkId) {
        final CheckId cid = new CheckId(checkId);
        final Check check = receiptRepository.find(cid);
        final PurchaseDTOAssembler assembler = new PurchaseDTOAssembler();
        return check.products().items().stream()
                .map(i -> {
                    final PurchaseId pid = PurchaseId.nextId();
                    final Purchase p = new Purchase(pid, i.name(), check.dateTime(), i.price(), BigDecimal.valueOf(i.quantity()));
                    p.assignCheck(cid);
                    purchaseRepository.store(p);
                    return assembler.toDTO(p);
                })
                .collect(Collectors.toList());
    }
}
