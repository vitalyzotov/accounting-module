package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.vzotov.banking.domain.model.Card;

import java.util.List;

@Service
public class CardGuardImpl implements CardGuard {

    @Override
    @PostFilter("hasAuthority(filterObject.owner().authority())")
    public List<Card> filter(List<Card> list) {
        return list;
    }

    @Override
    @PreAuthorize("hasAuthority(#entity.owner().authority())")
    public Card accessing(Card entity) {
        return entity;
    }
}
