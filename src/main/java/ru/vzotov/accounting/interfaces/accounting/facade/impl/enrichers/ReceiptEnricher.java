package ru.vzotov.accounting.interfaces.accounting.facade.impl.enrichers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.ReceiptRef;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.QRCodeDTOAssembler;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.cashreceipt.domain.model.QRCode;
import ru.vzotov.cashreceipt.domain.model.QRCodeRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

public class ReceiptEnricher extends AbstractEnricher<ReceiptRef> {

    private final QRCodeRepository repository;

    private final Map<String, QRCode> cache;

    private final QRCodeDTOAssembler assembler = new QRCodeDTOAssembler();

    public ReceiptEnricher(QRCodeRepository repository) {
        this(repository, Collections.emptySet());
    }

    public ReceiptEnricher(QRCodeRepository repository, Collection<QRCode> cache) {
        this.repository = repository;
        this.cache = cache.stream().collect(Collectors.toMap(it -> it.receiptId().value(), identity()));
    }


    @Override
    public ReceiptRef apply(ReceiptRef ref) {
        return ref == null ? null : assembler.toDTO(
                cache.computeIfAbsent(ref.getReceiptId(), id -> repository.find(new ReceiptId(id)))
        );
    }
}
