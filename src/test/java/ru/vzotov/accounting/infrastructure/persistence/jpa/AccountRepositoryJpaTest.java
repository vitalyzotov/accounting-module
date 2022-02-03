package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.banking.domain.model.Account;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.BankId;
import ru.vzotov.banking.domain.model.CardNumber;
import ru.vzotov.person.domain.model.PersonId;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class AccountRepositoryJpaTest {

    private static final Logger log = LoggerFactory.getLogger(AccountRepositoryJpaTest.class);

    private static final String PERSON_ID = "c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2";

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void find() {
        Account account = accountRepository.find(new AccountNumber("40817810108290123456"));
        log.info("Account loaded {}", account);
        assertThat(account).isNotNull();
    }

    @Test
    public void findAll() {
        List<Account> all = accountRepository.findAll(Collections.singleton(new PersonId(PERSON_ID)));
        assertThat(all).isNotNull();
        assertThat(all).isNotEmpty();
        assertThat(all.size()).isEqualTo((int) all.stream().map(Account::accountNumber).map(AccountNumber::number).distinct().count());
    }

    @Test
    public void store() {
        Account account = new Account(new AccountNumber("40817810808290123456"), "Счет кредитной карты", PersonId.nextId());
        accountRepository.update(account);
    }

    @Test
    public void findAccountOfCard() {
        Account account1 = accountRepository.findAccountOfCard(new CardNumber("4154822022031234"), LocalDate.of(2020, Month.JANUARY, 3));
        assertThat(account1).isNotNull();

        Account account2 = accountRepository.findAccountOfCard(new CardNumber("4154822022031234"), LocalDate.of(2020, Month.JANUARY, 1));
        assertThat(account2).isNull();

        Account account3 = accountRepository.findAccountOfCard(new CardNumber("4154822022031234"), LocalDate.of(2020, Month.JANUARY, 10));
        assertThat(account1).isNotNull();
    }

    @Test
    public void findAccountByBankAndCurrency() {
        assertThat(accountRepository.find(BankId.TINKOFF, Currency.getInstance("RUR")))
                .isNotEmpty()
                .map(Account::accountNumber)
                .contains(new AccountNumber("40817810000016123457"));
    }

    @Test
    public void findAccountByAlias() {
        assertThat(accountRepository.findByAlias("17416128")).containsOnly(new Account(new AccountNumber("40817810418370123456"), PersonId.nextId()));
        assertThat(accountRepository.findByAlias("17416127")).containsOnly(new Account(new AccountNumber("40817810418370123456"), PersonId.nextId()));
    }
}
