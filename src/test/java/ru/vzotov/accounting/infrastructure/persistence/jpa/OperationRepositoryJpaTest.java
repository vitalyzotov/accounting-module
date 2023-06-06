package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.AccountRepository;
import ru.vzotov.accounting.domain.model.BudgetCategoryRepository;
import ru.vzotov.accounting.domain.model.OperationRepository;
import ru.vzotov.banking.domain.model.Account;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.BudgetCategory;
import ru.vzotov.banking.domain.model.BudgetCategoryId;
import ru.vzotov.banking.domain.model.Operation;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.domain.model.Money;
import ru.vzotov.person.domain.model.PersonId;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class OperationRepositoryJpaTest {
    private static final Logger log = LoggerFactory.getLogger(OperationRepositoryJpaTest.class);

    private static final PersonId PERSON_ID = new PersonId("c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2");

    @Autowired
    private BudgetCategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OperationRepository repository;

    @Test
    public void find() {
        OperationId operationId = new OperationId("test-operation-1");
        Operation operation = repository.find(operationId);
        log.info("Operation loaded {}", operation);
        assertThat(operation).isNotNull();
        assertThat(operation.operationId()).isNotNull().isEqualTo(operationId);
        assertThat(operation.date()).isEqualTo(LocalDate.of(2017, 7, 10));
        assertThat(operation.category()).isEqualTo(new BudgetCategoryId(781381038049753674L));
        assertThat(operation.amount()).isEqualTo(Money.rubles(10d));
        assertThat(operation.type()).isEqualTo(OperationType.WITHDRAW);
        assertThat(operation.account()).isEqualTo(new AccountNumber("40817810108290123456"));
    }

    @Test
    public void findByDateAndAmount() {
        final List<Operation> operations = repository.findByDateAndAmount(
                Collections.singleton(PERSON_ID),
                LocalDate.of(2017, 7, 10),
                LocalDate.of(2017, 7, 10),
                Money.rubles(10d));
        assertThat(operations)
                .isNotEmpty()
                .allSatisfy(operation -> {
                    assertThat(operation).isNotNull();
                    assertThat(operation.operationId()).isNotNull();
                    assertThat(operation.date()).isEqualTo(LocalDate.of(2017, 7, 10));
                    assertThat(operation.amount()).isEqualTo(Money.rubles(10d));

                    final Account account = accountRepository.find(operation.account());
                    assertThat(account.owner())
                            .as("Wrong owner of operation %s", operation)
                            .isEqualTo(PERSON_ID);
                });
    }

    @Test
    public void findByDate() {
        final List<Operation> operations = repository.findByDate(
                Collections.singleton(PERSON_ID),
                LocalDate.of(2017, 7, 10),
                LocalDate.of(2017, 7, 10));
        assertThat(operations)
                .isNotEmpty()
                .allSatisfy(operation -> {
                    assertThat(operation.operationId()).isNotNull();
                    assertThat(operation.date()).isEqualTo(LocalDate.of(2017, 7, 10));

                    final Account account = accountRepository.find(operation.account());
                    assertThat(account.owner())
                            .as("Wrong owner of operation %s", operation)
                            .isEqualTo(PERSON_ID);
                });
    }

    @Test
    public void findByAccountAndDate() {
        final AccountNumber accountNumber = new AccountNumber("40817810108290123456");
        final List<Operation> operations = repository.findByAccountAndDate(
                accountNumber,
                LocalDate.of(2017, 7, 10),
                LocalDate.of(2017, 7, 10));

        assertThat(operations)
                .isNotEmpty()
                .allSatisfy(operation -> {
                    assertThat(operation).isNotNull();
                    assertThat(operation.operationId()).isNotNull();
                    assertThat(operation.date()).isEqualTo(LocalDate.of(2017, 7, 10));
                    assertThat(operation.account()).isEqualTo(accountNumber);
                });
    }

    @Test
    public void store() {
        Operation operation = new Operation(
                new OperationId("test-operation-2"),
                LocalDate.of(2018, 7, 10),
                Money.rubles(10),
                OperationType.WITHDRAW,
                new AccountNumber("40817810108290123456"),
                new String(new char[512]).replace('\0', 'T'), // 512 chars length description
                new BudgetCategoryId(781381038049753674L)
        );
        repository.store(operation);
    }

    @Test
    public void assignCategory() {
        Operation operation = repository.find(new OperationId("tx-operation-1"));
        operation.assignCategory(new BudgetCategoryId(781381038049753674L));
        repository.store(operation);
    }

    @Test
    public void deleteCategory() {
        assertThatThrownBy(() -> {
            BudgetCategory category = categoryRepository.find(new BudgetCategoryId(781381038049753674L));
            categoryRepository.delete(category);
        }).isInstanceOf(PersistenceException.class);
    }

    @Test
    public void findWithoutDeals() {
        final List<Operation> operations = repository.findWithoutDeals();
        log.info(operations.toString());
        assertThat(operations).isNotEmpty();
        assertThat(operations).map(Operation::operationId)
                .doesNotContain(new OperationId("test-operation-1"))
                .doesNotContain(new OperationId("deal-operation-2"));
    }

}
