package ru.vzotov.accounting.interfaces.accounting.facade.impl.enrichers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.ReceiptRef;
import ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers.QRCodeDTOAssembler;
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.CheckQRCode;
import ru.vzotov.cashreceipt.domain.model.QRCodeRepository;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

public class ReceiptEnricher extends AbstractEnricher<ReceiptRef> {

    private final QRCodeRepository repository;

    private final Map<String, CheckQRCode> cache;

    private final QRCodeDTOAssembler assembler = new QRCodeDTOAssembler();

    public ReceiptEnricher(QRCodeRepository repository, Collection<CheckQRCode> cache) {
        this.repository = repository;
        this.cache = cache.stream().collect(Collectors.toMap(it -> it.checkId().value(), identity()));
    }


    @Override
    public ReceiptRef apply(ReceiptRef ref) {
        return ref == null ? null : assembler.toDTO(
                cache.computeIfAbsent(ref.getCheckId(), id -> repository.find(new CheckId(id)))
        );
    }
}
