package ru.vzotov.accounting.interfaces.accounting.facade.impl.enrichers;

import ru.vzotov.accounting.domain.model.CardOperationRepository;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.OperationAssembler;
import ru.vzotov.accounting.interfaces.common.enrichers.AbstractEnricher;
import ru.vzotov.banking.domain.model.CardOperation;
import ru.vzotov.banking.domain.model.OperationId;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

public class CardOperationEnricher extends AbstractEnricher<AccountingApi.OperationRef> {

    private final CardOperationRepository repository;

    private final Map<String, CardOperation> cache;

    public CardOperationEnricher(CardOperationRepository repository) {
        this(repository, Collections.emptySet());
    }

    public CardOperationEnricher(CardOperationRepository repository, Collection<CardOperation> cache) {
        this.repository = repository;
        this.cache = cache.stream().collect(Collectors.toMap(op -> op.operationId().idString(), identity()));
    }

    public AccountingApi.CardOperation apply(AccountingApi.OperationRef ref) {
        return ref == null ? null : OperationAssembler.toDTO(
                cache.computeIfAbsent(ref.operationId(), id -> repository.find(new OperationId(id)))
        );
    }

}
