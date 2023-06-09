package ru.vzotov.accounting.interfaces.accounting.facade.impl.enrichers;

import ru.vzotov.accounting.interfaces.common.enrichers.AbstractEnricher;
import ru.vzotov.accounting.interfaces.purchases.PurchasesApi;
import ru.vzotov.accounting.interfaces.purchases.facade.impl.assembler.PurchaseAssembler;
import ru.vzotov.purchase.domain.model.Purchase;
import ru.vzotov.purchase.domain.model.PurchaseId;
import ru.vzotov.purchases.domain.model.PurchaseRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

public class PurchaseEnricher extends AbstractEnricher<PurchasesApi.PurchaseRef> {

    private final PurchaseRepository repository;

    private final Map<String, Purchase> cache;

    private final PurchaseAssembler assembler = new PurchaseAssembler();

    public PurchaseEnricher(PurchaseRepository repository) {
        this(repository, Collections.emptySet());
    }

    public PurchaseEnricher(PurchaseRepository repository, Collection<Purchase> cache) {
        this.repository = repository;
        this.cache = cache.stream().collect(Collectors.toMap(it -> it.purchaseId().value(), identity()));
    }

    @Override
    public PurchasesApi.PurchaseRef apply(PurchasesApi.PurchaseRef ref) {
        return ref == null ? null : assembler.toDTO(
                cache.computeIfAbsent(ref.purchaseId(), id -> repository.find(new PurchaseId(id)))
        );
    }
}
