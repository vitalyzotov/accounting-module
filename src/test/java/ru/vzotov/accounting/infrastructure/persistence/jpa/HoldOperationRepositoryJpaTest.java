package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.HoldOperationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.HoldId;
import ru.vzotov.banking.domain.model.HoldOperation;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.domain.model.Money;
import ru.vzotov.person.domain.model.PersonId;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class HoldOperationRepositoryJpaTest {
    private static final Logger log = LoggerFactory.getLogger(HoldOperationRepositoryJpaTest.class);
    private static final PersonId PERSON_ID = new PersonId("c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2");

    @Autowired
    private HoldOperationRepository repository;

    @Test
    public void testConstruct() {
        AccountNumber account = new AccountNumber("40817810108290123456");
        HoldOperation hold = new HoldOperation(
                HoldId.nextId(),
                account,
                LocalDate.of(2018, 7, 8),
                Money.rubles(2007.3d),
                OperationType.WITHDRAW,
                "11276718 RU MAGNIT MM ANTROPOS.>Sar 18.07.08 18.07.08 2007.30 RUR 555957++++++1234",
                null,
                null
        );

        repository.store(hold);
    }

    @Test
    public void testFind() {
        HoldOperation hold = repository.find(
                new AccountNumber("40817810108290123456"), LocalDate.of(2020, Month.FEBRUARY, 27),
                Money.rubles(1166d), OperationType.WITHDRAW,
                "20603703 RU IP FEJZULLAEVA O.B.>Sar 20.02.27 20.02.27 1166.00 RUR 555957++++++1234 (Android Pay-9313)");
        assertThat(hold).isNotNull();
    }

    @Test
    public void testFindByAccount() {
        AccountNumber accountNumber = new AccountNumber("40817810108290123456");
        List<HoldOperation> list = repository.findByAccountAndDate(
                accountNumber,
                LocalDate.of(2020, Month.FEBRUARY, 27),
                LocalDate.of(2020, Month.FEBRUARY, 29)
        );
        assertThat(list).isNotEmpty().hasSize(2);
    }

}
