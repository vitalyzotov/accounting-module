package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.BankRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.banking.domain.model.Bank;
import ru.vzotov.banking.domain.model.BankId;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class BankRepositoryJpaTest {
    private static final Logger log = LoggerFactory.getLogger(BankRepositoryJpaTest.class);

    @Autowired
    private BankRepository bankRepository;

    @Test
    public void testConstruct() {
        Bank bank = new Bank(new BankId("044525974"), "Тинькофф", "АО \"Тинькофф Банк\"", "Акционерное общество \"Тинькофф Банк\"");
        bankRepository.store(bank);
    }

    @Test
    public void testFind() {
        Bank bank = bankRepository.find(new BankId("044525593"));
        assertThat(bank).isNotNull();
    }

    @Test
    public void testFindAll() {
        List<Bank> list = bankRepository.findAll();
        assertThat(list).isNotEmpty();
    }

}
