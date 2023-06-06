package ru.vzotov.accounting.interfaces.accounting.facade.impl.enrichers;

import ru.vzotov.accounting.domain.model.OperationRepository;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.AccountOperationDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.OperationRef;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.OperationDTOAssembler;
import ru.vzotov.banking.domain.model.Operation;
import ru.vzotov.banking.domain.model.OperationId;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

public class OperationEnricher extends AbstractEnricher<OperationRef> {

    private final OperationRepository repository;

    private final Map<String, Operation> cache;

    public OperationEnricher(OperationRepository repository) {
        this(repository, Collections.emptySet());
    }

    public OperationEnricher(OperationRepository repository, Collection<Operation> cache) {
        this.repository = repository;
        this.cache = cache.stream().collect(Collectors.toMap(op -> op.operationId().idString(), identity()));
    }

    public AccountOperationDTO apply(OperationRef ref) {
        return ref == null ? null : OperationDTOAssembler.toDTO(
                cache.computeIfAbsent(ref.operationId(), id -> repository.find(new OperationId(id)))
        );
    }

}
