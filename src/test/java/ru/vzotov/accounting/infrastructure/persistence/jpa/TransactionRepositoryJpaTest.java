package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.TransactionRepository;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.Transaction;
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
public class TransactionRepositoryJpaTest {

    private static final PersonId PERSON_ID = new PersonId("c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2");

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void testConstruct() {
        OperationId primary = new OperationId("tx-operation-1");
        OperationId secondary = new OperationId("tx-operation-2");
        Transaction tx = new Transaction(primary, secondary);
        transactionRepository.store(tx);
    }

    @Test
    public void testFindByDate() {
        List<Transaction> txList = transactionRepository.findByDate(
                Collections.singleton(PERSON_ID),
                LocalDate.of(2019, Month.AUGUST, 1),
                LocalDate.of(2019, Month.AUGUST, 3)
        );
        assertThat(txList).isNotEmpty();
        assertThat(txList.get(0).primaryOperation()).isEqualTo(new OperationId("tx-operation-3"));
    }

    @Test
    public void testFindByOpId() {
        Transaction tx3 = transactionRepository.find(new OperationId("tx-operation-3"));
        Transaction tx4 = transactionRepository.find(new OperationId("tx-operation-4"));
        assertThat(tx3).isNotNull().isEqualTo(tx4);
    }

}
