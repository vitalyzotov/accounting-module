package ru.vzotov.accounting.interfaces.accounting.facade.impl.enrichers;

import ru.vzotov.accounting.domain.model.CardOperationRepository;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.CardOperationDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.OperationRef;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.OperationDTOAssembler;
import ru.vzotov.banking.domain.model.CardOperation;
import ru.vzotov.banking.domain.model.OperationId;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

public class CardOperationEnricher extends AbstractEnricher<OperationRef> {

    private final CardOperationRepository repository;

    private final Map<String, CardOperation> cache;

    public CardOperationEnricher(CardOperationRepository repository) {
        this(repository, Collections.emptySet());
    }

    public CardOperationEnricher(CardOperationRepository repository, Collection<CardOperation> cache) {
        this.repository = repository;
        this.cache = cache.stream().collect(Collectors.toMap(op -> op.operationId().idString(), identity()));
    }

    public CardOperationDTO apply(OperationRef ref) {
        return ref == null ? null : OperationDTOAssembler.toDTO(
                cache.computeIfAbsent(ref.getOperationId(), id -> repository.find(new OperationId(id)))
        );
    }

}
