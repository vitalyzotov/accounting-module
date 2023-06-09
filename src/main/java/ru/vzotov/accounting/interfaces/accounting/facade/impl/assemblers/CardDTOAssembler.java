package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.banking.domain.model.AccountBinding;
import ru.vzotov.banking.domain.model.Card;

import java.util.Comparator;
import java.util.stream.Collectors;

public class CardDTOAssembler {

    public static AccountingApi.Card toDTO(Card card) {
        return card == null ? null : new AccountingApi.Card(
                card.cardNumber().value(),
                card.owner().value(),
                card.validThru().atEndOfMonth(),
                card.issuer().value(),
                card.accounts() == null ? null : card.accounts().stream()
                        .sorted(Comparator.comparing(AccountBinding::from))
                        .map(AccountBindingDTOAssembler::toDTO)
                        .collect(Collectors.toList())
        );
    }
}
