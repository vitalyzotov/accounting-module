package ru.vzotov.accounting.interfaces.accounting.facade.impl.enrichers;

import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseRef;
import ru.vzotov.accounting.interfaces.purchases.facade.impl.assembler.PurchaseDTOAssembler;
import ru.vzotov.purchase.domain.model.Purchase;
import ru.vzotov.purchase.domain.model.PurchaseId;
import ru.vzotov.purchases.domain.model.PurchaseRepository;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

public class PurchaseEnricher extends AbstractEnricher<PurchaseRef> {

    private final PurchaseRepository repository;

    private final Map<String, Purchase> cache;

    private final PurchaseDTOAssembler assembler = new PurchaseDTOAssembler();

    public PurchaseEnricher(PurchaseRepository repository, Collection<Purchase> cache) {
        this.repository = repository;
        this.cache = cache.stream().collect(Collectors.toMap(it -> it.purchaseId().value(), identity()));
    }

    @Override
    public PurchaseRef apply(PurchaseRef ref) {
        return ref == null ? null : assembler.toDTO(
                cache.computeIfAbsent(ref.getPurchaseId(), id -> repository.find(new PurchaseId(id)))
        );
    }
}
