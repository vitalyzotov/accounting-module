package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.domain.model.Deal;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealDTO;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.purchase.domain.model.PurchaseId;

import java.util.stream.Collectors;

public class DealDTOAssembler {
    public static DealDTO toDTO(Deal deal) {
        return deal == null ? null : new DealDTO(
                deal.dealId().value(),
                deal.date(),
                MoneyDTOAssembler.toDTO(deal.amount()),
                deal.description(),
                deal.comment(),
                deal.category() == null ? null : deal.category().id(),
                deal.receipts().stream().map(CheckId::value).collect(Collectors.toList()),
                deal.operations().stream().map(OperationId::idString).collect(Collectors.toList()),
                deal.purchases().stream().map(PurchaseId::value).collect(Collectors.toList())
        );
    }
}
