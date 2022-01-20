package ru.vzotov.accounting.domain.model;

import ru.vzotov.banking.domain.model.BankId;
import ru.vzotov.banking.domain.model.Card;
import ru.vzotov.banking.domain.model.CardNumber;
import ru.vzotov.person.domain.model.PersonId;

import java.util.List;

public interface CardRepository {

    Card find(CardNumber cardNumber);

    /**
     * Поиск карты по маске
     *
     * Звездочка (*) означает любое количество цифр
     * Плюс (+) означает одну любую цифру
     *
     * @param numberMask маска номера карты
     * @return список найденных карт
     */
    List<Card> findByMask(String numberMask);

    List<Card> findAll();

    List<Card> find(PersonId owner);

    List<Card> findByBank(PersonId owner, BankId bankId);

    void store(Card card);

    void delete(Card card);

}
