package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.domain.model.Deal;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealDTO;
import ru.vzotov.accounting.interfaces.purchases.facade.impl.assembler.PurchaseDTOAssembler;

import java.util.stream.Collectors;

public class DealDTOAssembler {

    private static final QRCodeDTOAssembler receiptAssembler = new QRCodeDTOAssembler();
    private static final PurchaseDTOAssembler purchaseAssembler = new PurchaseDTOAssembler();

    public static DealDTO toDTO(Deal deal) {
        return deal == null ? null : new DealDTO(
                deal.dealId().value(),
                deal.owner().value(),
                deal.date(),
                MoneyDTOAssembler.toDTO(deal.amount()),
                deal.description(),
                deal.comment(),
                deal.category() == null ? null : deal.category().id(),
                deal.receipts().stream()
                        .map(receiptAssembler::toRef)
                        .collect(Collectors.toList()),
                deal.operations().stream()
                        .map(OperationDTOAssembler::toRef)
                        .collect(Collectors.toList()),
                deal.cardOperations().stream()
                        .map(OperationDTOAssembler::toRef)
                        .collect(Collectors.toList()),
                deal.purchases().stream()
                        .map(purchaseAssembler::toRef)
                        .collect(Collectors.toList())
        );
    }
}
