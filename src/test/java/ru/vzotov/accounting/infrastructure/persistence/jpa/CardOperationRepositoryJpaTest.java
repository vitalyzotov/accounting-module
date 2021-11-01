package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.AccountRepository;
import ru.vzotov.accounting.domain.model.CardOperationRepository;
import ru.vzotov.accounting.domain.model.OperationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.CardNumber;
import ru.vzotov.banking.domain.model.CardOperation;
import ru.vzotov.banking.domain.model.City;
import ru.vzotov.banking.domain.model.Country;
import ru.vzotov.banking.domain.model.MccCode;
import ru.vzotov.banking.domain.model.Merchant;
import ru.vzotov.banking.domain.model.Operation;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.banking.domain.model.PosTerminal;
import ru.vzotov.banking.domain.model.PosTerminalId;
import ru.vzotov.banking.domain.model.Street;
import ru.vzotov.domain.model.Money;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(JpaConfig.class)
@SpringBootTest
@Transactional
public class CardOperationRepositoryJpaTest {

    private static final Logger log = LoggerFactory.getLogger(CardOperationRepositoryJpaTest.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardOperationRepository repository;

    @Autowired
    private OperationRepository operationRepository;

    @Test
    public void find() {
        OperationId operationId = new OperationId("test-operation-1");
        CardOperation operation = repository.find(operationId);
        log.info("Operation loaded {}", operation);
        assertThat(operation).isNotNull();
        assertThat(operation.operationId()).isNotNull().isEqualTo(operationId);
        assertThat(operation.authDate()).isEqualTo(LocalDate.of(2018, 7, 10));
        assertThat(operation.purchaseDate()).isEqualTo(LocalDate.of(2018, 7, 7));
        assertThat(operation.amount()).isEqualTo(Money.rubles(500d));
    }

    @Test
    public void findByDateAndAmount() {
        final List<CardOperation> operations = repository.findByDateAndAmount(
                LocalDate.of(2018, 7, 7),
                LocalDate.of(2018, 7, 8),
                Money.rubles(500d));
        assertThat(operations).isNotEmpty();

        final CardOperation operation = operations.get(0);

        log.info("Operation loaded {}", operation);
        assertThat(operation).isNotNull();
        assertThat(operation.operationId()).isNotNull();
        assertThat(operation.purchaseDate()).isEqualTo(LocalDate.of(2018, 7, 7));
        assertThat(operation.amount()).isEqualTo(Money.rubles(500d));
    }

    @Test
    public void store() {
        Operation operation = new Operation(
                new OperationId("test-operation-3"),
                LocalDate.of(2018, 7, 10),
                Money.rubles(10),
                OperationType.WITHDRAW,
                accountRepository.find(new AccountNumber("40817810108290123456")),
                "Тестовая операция"
        );
        operationRepository.store(operation);

        CardOperation cardOperation = new CardOperation(
                operation.operationId(),
                new CardNumber("555957++++++1234"),
                new PosTerminal(new PosTerminalId("11276718"), new Country("RUS"), new City("SARATOV"), new Street("6 MOT"), new Merchant("MAGNIT MM ANT")),
                LocalDate.of(2018, 7, 11),
                LocalDate.of(2018, 7, 8),
                Money.rubles(2007.3d), "Google pay-9313", new MccCode("5411")
        );
        repository.store(cardOperation);
    }

    @Test
    public void storeWithoutTerminal() {
        Operation operation = new Operation(
                new OperationId("test-operation-4"),
                LocalDate.of(2018, 7, 10),
                Money.rubles(10),
                OperationType.WITHDRAW,
                accountRepository.find(new AccountNumber("40817810108290123456")),
                "Тестовая операция"
        );
        operationRepository.store(operation);

        CardOperation cardOperation = new CardOperation(
                operation.operationId(),
                new CardNumber("555957++++++1234"),
                null,
                LocalDate.of(2018, 7, 11),
                LocalDate.of(2018, 7, 8),
                Money.rubles(2007.3d), null, new MccCode("5411")
        );
        repository.store(cardOperation);
    }
}
