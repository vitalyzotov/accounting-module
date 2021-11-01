package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.CardRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.BankId;
import ru.vzotov.banking.domain.model.Card;
import ru.vzotov.banking.domain.model.CardNumber;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(JpaConfig.class)
@SpringBootTest
@Transactional
public class CardRepositoryJpaTest {
    private static final Logger log = LoggerFactory.getLogger(CardRepositoryJpaTest.class);

    @Autowired
    private CardRepository cardRepository;

    @Test
    public void testConstruct() {
        Card card = new Card(
                new CardNumber("5559572051681234"), YearMonth.of(2021, Month.FEBRUARY),
                new BankId("044525593"));
        card.bindToAccount(new AccountNumber("40817810108290123456"), LocalDate.of(2017, Month.JANUARY, 1), card.validThru().atEndOfMonth());
        cardRepository.store(card);
    }

    @Test
    public void testFind() {
        List<Card> cards = cardRepository.findByBank(new BankId("044525593"));
        assertThat(cards).isNotEmpty().contains(new Card(new CardNumber("4154822022031234"), YearMonth.now(), new BankId("044525593")));
    }

    @Test
    public void testFindAll() {
        List<Card> list = cardRepository.findAll();
        assertThat(list).isNotEmpty();
    }

}
