package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.CardRepository;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.BankId;
import ru.vzotov.banking.domain.model.Card;
import ru.vzotov.banking.domain.model.CardNumber;
import ru.vzotov.person.domain.model.PersonId;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class CardRepositoryJpaTest {
    private static final Logger log = LoggerFactory.getLogger(CardRepositoryJpaTest.class);

    private static final PersonId PERSON_ID = new PersonId("c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2");

    @Autowired
    private CardRepository cardRepository;

    @Test
    public void testConstruct() {
        Card card = new Card(
                new CardNumber("5559572051681234"),
                PERSON_ID,
                YearMonth.of(2021, Month.FEBRUARY),
                new BankId("044525593"));
        card.bindToAccount(new AccountNumber("40817810108290123456"), LocalDate.of(2017, Month.JANUARY, 1), card.validThru().atEndOfMonth());
        cardRepository.store(card);
    }

    @Test
    public void testFind() {
        List<Card> cards = cardRepository.findByBank(PERSON_ID, new BankId("044525593"));
        assertThat(cards).isNotEmpty()
                .contains(new Card(new CardNumber("4154822022031234"), PERSON_ID, YearMonth.now(), new BankId("044525593")));
    }

    @Test
    public void testFindAll() {
        List<Card> list = cardRepository.findAll();
        assertThat(list).isNotEmpty();
    }

}
