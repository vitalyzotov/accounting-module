package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.domain.model.Deal;
import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.domain.model.DealRepository;
import ru.vzotov.accounting.interfaces.accounting.facade.DealsSharedFacade;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;
import ru.vzotov.accounting.interfaces.purchases.facade.impl.assembler.PurchaseDTOAssembler;
import ru.vzotov.purchases.domain.model.PurchaseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DealsSharedFacadeImpl implements DealsSharedFacade {

    private final DealRepository dealRepository;

    private final PurchaseRepository purchaseRepository;

    public DealsSharedFacadeImpl(DealRepository dealRepository, PurchaseRepository purchaseRepository) {
        this.dealRepository = dealRepository;
        this.purchaseRepository = purchaseRepository;
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
}
