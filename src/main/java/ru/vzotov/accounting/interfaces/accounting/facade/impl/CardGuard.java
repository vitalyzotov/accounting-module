package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import ru.vzotov.banking.domain.model.Card;

import java.util.List;

public interface CardGuard {

    List<Card> filter(List<Card> list);

    Card accessing(Card entity);
}
