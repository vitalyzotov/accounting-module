package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.CardDTO;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.CardCreateRequest;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.CardStoreResponse;
import ru.vzotov.banking.domain.model.BankId;
import ru.vzotov.banking.domain.model.CardNumber;
import ru.vzotov.person.domain.model.PersonId;

import java.util.List;

@RestController
@RequestMapping("/accounting/cards")
@CrossOrigin
public class CardsController {

    @Autowired
    private AccountingFacade accountingFacade;

    @GetMapping
    public List<CardDTO> listCards(@RequestParam(required = false) String issuer) {
        return accountingFacade.listCards(issuer == null ? null : new BankId(issuer));
    }

    @GetMapping("{cardNumber}")
    public CardDTO getCard(@PathVariable String cardNumber) {
        return accountingFacade.getCard(new CardNumber(cardNumber));
    }

    @PostMapping
    public CardStoreResponse createCard(@RequestBody CardCreateRequest card) {
        CardNumber cardNumber = accountingFacade.createCard(
                new CardNumber(card.getCardNumber()),
                new PersonId(card.getOwner()),
                card.getValidThru(),
                new BankId(card.getIssuer()),
                card.getAccounts()
        );
        return new CardStoreResponse(cardNumber.value());
    }

    @PutMapping("{cardNumber}")
    public CardStoreResponse updateCard(@PathVariable String cardNumber, @RequestBody CardCreateRequest card) {
        if (!cardNumber.equals(card.getCardNumber())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        final CardNumber number = accountingFacade.modifyCard(
                new CardNumber(card.getCardNumber()),
                new PersonId(card.getOwner()),
                card.getValidThru(),
                new BankId(card.getIssuer()),
                card.getAccounts()
        );
        return new CardStoreResponse(number.value());
    }

    @DeleteMapping("{cardNumber}")
    public CardStoreResponse deleteCard(@PathVariable String cardNumber) {
        CardNumber id = accountingFacade.deleteCard(new CardNumber(cardNumber));
        return new CardStoreResponse(id.value());
    }

}
