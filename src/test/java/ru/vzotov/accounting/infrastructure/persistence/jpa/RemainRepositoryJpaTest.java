package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.assertj.core.data.Index;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.Remain;
import ru.vzotov.accounting.domain.model.RemainId;
import ru.vzotov.accounting.domain.model.RemainRepository;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.domain.model.Money;
import ru.vzotov.person.domain.model.PersonId;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class RemainRepositoryJpaTest {

    private static final Logger log = LoggerFactory.getLogger(RemainRepositoryJpaTest.class);

    private static final String PERSON_ID = "c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2";

    @Autowired
    private RemainRepository remainRepository;

    @Test
    public void testConstruct() {
        Remain remain = new Remain(new AccountNumber("40817810108290123456"), LocalDate.of(2020, Month.FEBRUARY, 1), Money.kopecks(20000));
        remainRepository.store(remain);
    }

    @Test
    public void testFind() {
        List<Remain> remains = remainRepository.find(
                Collections.singleton(new PersonId(PERSON_ID)),
                Collections.singleton(new AccountNumber("40817810418370123456")),
                null,
                null,
                true
        );
        assertThat(remains)
                .hasSize(1)
                .satisfies(
                        r -> assertThat(r.date()).isEqualTo("2019-08-10"),
                        Index.atIndex(0)
                );

        remains = remainRepository.find(
                Collections.singleton(new PersonId(PERSON_ID)),
                Collections.singleton(new AccountNumber("40817810418370123456")),
                LocalDate.of(2019, Month.AUGUST, 1),
                LocalDate.of(2019, Month.AUGUST, 10),
                false
        );
        assertThat(remains)
                .hasSize(2);
    }

    @Test
    public void testFindByDate() {
        List<Remain> remains = remainRepository.findByDate(
                Collections.singleton(new PersonId(PERSON_ID)),
                LocalDate.of(2019, Month.AUGUST, 1),
                LocalDate.of(2019, Month.AUGUST, 3));
        assertThat(remains).isNotEmpty();
        assertThat(remains.get(0).value()).isEqualTo(Money.kopecks(30000));
    }

    @Test
    public void testFindByAccount() {
        AccountNumber acc = new AccountNumber("40817810418370123456");
        List<Remain> remains = remainRepository.findByAccountNumber(acc);
        assertThat(remains).isNotEmpty();
        assertThat(remains.get(0).account()).isEqualTo(acc);
    }

    @Test
    public void testFindById() {
        AccountNumber acc = new AccountNumber("40817810418370123456");
        RemainId id = new RemainId(acc, LocalDate.of(2019, Month.AUGUST, 2));
        Remain remain = remainRepository.find(id);
        assertThat(remain).isNotNull();
    }

}
