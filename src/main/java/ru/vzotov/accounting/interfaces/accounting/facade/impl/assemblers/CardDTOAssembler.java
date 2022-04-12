package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.CardDTO;
import ru.vzotov.banking.domain.model.AccountBinding;
import ru.vzotov.banking.domain.model.Card;

import java.util.Comparator;
import java.util.stream.Collectors;

public class CardDTOAssembler {

    public static CardDTO toDTO(Card card) {
        return card == null ? null : new CardDTO(
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
