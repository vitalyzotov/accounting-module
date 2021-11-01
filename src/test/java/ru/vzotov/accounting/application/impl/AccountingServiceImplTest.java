package ru.vzotov.accounting.application.impl;

import ru.vzotov.accounting.application.AccountNotFoundException;
import ru.vzotov.accounting.application.AccountingService;
import ru.vzotov.accounting.domain.model.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.banking.domain.model.Account;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.banking.domain.model.CardNumber;
import ru.vzotov.banking.domain.model.City;
import ru.vzotov.banking.domain.model.Country;
import ru.vzotov.banking.domain.model.MccCode;
import ru.vzotov.banking.domain.model.Merchant;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.banking.domain.model.OperationType;
import ru.vzotov.banking.domain.model.PosTerminal;
import ru.vzotov.banking.domain.model.PosTerminalId;
import ru.vzotov.banking.domain.model.Street;
import ru.vzotov.banking.domain.model.TransactionReference;
import ru.vzotov.domain.model.Money;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountingServiceImplTest {

    @Autowired
    private AccountingService accountingService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void registerOperation() {
        final AccountNumber accountNumber = new AccountNumber("40817810108290123457");

        Account account;
        account = accountRepository.find(accountNumber);
        assertThat(account).isNull();

        assertThatThrownBy(() -> {
            accountingService.registerOperation(
                    accountNumber,
                    LocalDate.of(2018, Month.JANUARY, 23),
                    new TransactionReference("REF001"),
                    OperationType.WITHDRAW,
                    Money.kopecks(10),
                    "Тестовая операция"
            );
        }).isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    public void registerCardOperation() throws AccountNotFoundException {
        OperationId id = accountingService.registerOperation(
                new AccountNumber("40817810108290123456"),
                LocalDate.of(2018, Month.MARCH, 6),
                new TransactionReference("REF002"),
                OperationType.WITHDRAW,
                Money.kopecks(100),
                "Тестовая операция"
        );

        OperationId cardOpId = accountingService.registerCardOperation(
                id,
                new CardNumber("2202051661361234"),
                new PosTerminal(new PosTerminalId("11276718"), new Country("RUS"), new City("SARATOV"), new Street("6 MOT"), new Merchant("MAGNIT MM ANT")),
                LocalDate.of(2018, Month.MARCH, 8),
                LocalDate.of(2018, Month.MARCH, 6),
                Money.kopecks(100),
                "Google pay-9313", new MccCode("5411")
        );

        assertThat(id).isEqualTo(cardOpId);
    }

    @Test
    public void registerCardOperationWithoutTerminal() throws AccountNotFoundException {
        OperationId id = accountingService.registerOperation(
                new AccountNumber("40817810108290123456"),
                LocalDate.of(2018, Month.MARCH, 7),
                new TransactionReference("REF003"),
                OperationType.WITHDRAW,
                Money.kopecks(100),
                "Тестовая операция"
        );

        OperationId cardOpId = accountingService.registerCardOperation(
                id,
                new CardNumber("2202051661361234"),
                null,
                LocalDate.of(2018, Month.MARCH, 8),
                LocalDate.of(2018, Month.MARCH, 7),
                Money.kopecks(100),
                "Google pay-9313", new MccCode("5411")
        );

        assertThat(id).isEqualTo(cardOpId);
    }
}
