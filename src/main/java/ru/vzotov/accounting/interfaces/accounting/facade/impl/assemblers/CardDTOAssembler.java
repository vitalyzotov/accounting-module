package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.CardDTO;
import ru.vzotov.banking.domain.model.Card;

public class CardDTOAssembler {

    public static CardDTO toDTO(Card card) {
        return card == null ? null : new CardDTO(
                card.cardNumber().value(),
                card.validThru().atEndOfMonth(),
                card.issuer().value()
        );
    }
}
