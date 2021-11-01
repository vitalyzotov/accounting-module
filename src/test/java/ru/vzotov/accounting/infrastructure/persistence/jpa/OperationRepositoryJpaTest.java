package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.AccountRepository;
import ru.vzotov.accounting.domain.model.BudgetCategoryRepository;
import ru.vzotov.accounting.domain.model.OperationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.banking.domain.model.Account;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.BudgetCategory;
import ru.vzotov.banking.domain.model.BudgetCategoryId;
import ru.vzotov.banking.domain.model.Operation;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.banking.domain.model.PersonId;
import ru.vzotov.domain.model.Money;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@Import(JpaConfig.class)
@SpringBootTest
@Transactional
public class OperationRepositoryJpaTest {
    private static final Logger log = LoggerFactory.getLogger(OperationRepositoryJpaTest.class);

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
        assertThat(operation.category()).isEqualTo(new BudgetCategory(new BudgetCategoryId(781381038049753674L), "Другое название категории"));
        assertThat(operation.amount()).isEqualTo(Money.rubles(10d));
        assertThat(operation.type()).isEqualTo(OperationType.WITHDRAW);
        assertThat(operation.account()).isEqualTo(new Account(new AccountNumber("40817810108290123456"), PersonId.nextId()));
    }

    @Test
    public void findByDateAndAmount() {
        final List<Operation> operations = repository.findByDateAndAmount(
                LocalDate.of(2017, 7, 10),
                LocalDate.of(2017, 7, 10),
                Money.rubles(10d));
        assertThat(operations).isNotEmpty();

        final Operation operation = operations.get(0);

        log.info("Operation loaded {}", operation);
        assertThat(operation).isNotNull();
        assertThat(operation.operationId()).isNotNull();
        assertThat(operation.date()).isEqualTo(LocalDate.of(2017, 7, 10));
        assertThat(operation.amount()).isEqualTo(Money.rubles(10d));
    }

    @Test
    public void findByDate() {
        final List<Operation> operations = repository.findByDate(
                LocalDate.of(2017, 7, 10),
                LocalDate.of(2017, 7, 10));
        assertThat(operations).isNotEmpty();

        final Operation operation = operations.get(0);

        log.info("Operation loaded {}", operation);
        assertThat(operation).isNotNull();
        assertThat(operation.operationId()).isNotNull();
        assertThat(operation.date()).isEqualTo(LocalDate.of(2017, 7, 10));
        assertThat(operation.amount()).isEqualTo(Money.rubles(10d));
    }

    @Test
    public void findByAccountAndDate() {
        final List<Operation> operations = repository.findByAccountAndDate(
                new AccountNumber("40817810108290123456"),
                LocalDate.of(2017, 7, 10),
                LocalDate.of(2017, 7, 10));
        assertThat(operations).isNotEmpty();

        final Operation operation = operations.get(0);

        log.info("Operation loaded {}", operation);
        assertThat(operation).isNotNull();
        assertThat(operation.operationId()).isNotNull();
        assertThat(operation.date()).isEqualTo(LocalDate.of(2017, 7, 10));
        assertThat(operation.amount()).isEqualTo(Money.rubles(10d));
    }

    @Test
    public void store() {
        Operation operation = new Operation(
                new OperationId("test-operation-2"),
                LocalDate.of(2018, 7, 10),
                Money.rubles(10),
                OperationType.WITHDRAW,
                accountRepository.find(new AccountNumber("40817810108290123456")),
                new String(new char[512]).replace('\0', 'T'), // 512 chars length description
                categoryRepository.find("Прочие расходы")
        );
        repository.store(operation);
    }

    @Test
    public void assignCategory() {
        Operation operation = repository.find(new OperationId("tx-operation-1"));
        BudgetCategory category = categoryRepository.find(new BudgetCategoryId(781381038049753674L));
        operation.assignCategory(category);
        repository.store(operation);
    }

    @Test
    public void deleteCategory() {
        assertThatThrownBy(() -> {
            BudgetCategory category = categoryRepository.find(new BudgetCategoryId(781381038049753674L));
            categoryRepository.delete(category);
        }).isInstanceOf(PersistenceException.class);
    }

}
