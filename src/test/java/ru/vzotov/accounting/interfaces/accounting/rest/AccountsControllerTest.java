package ru.vzotov.accounting.interfaces.accounting.rest;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.test.AbstractControllerTest;
import ru.vzotov.banking.domain.model.OperationType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class AccountsControllerTest extends AbstractControllerTest {

    @Test
    public void getAccount() {
        ResponseEntity<AccountingApi.Account> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts/40817810108290123456",
                        HttpMethod.GET, new HttpEntity<>(null),
                        AccountingApi.Account.class
                );
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody())
                .isNotNull()
                .hasFieldOrPropertyWithValue("number", "40817810108290123456");
    }

    @Test
    public void listAccounts() {
        ResponseEntity<List<AccountingApi.Account>> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts",
                        HttpMethod.GET, new HttpEntity<>(null),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).isNotEmpty();
    }

    @Test
    public void createAccount() {
        ResponseEntity<AccountingApi.Account.Ref> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts",
                        HttpMethod.POST, new HttpEntity<>(new AccountingApi.Account.Create("00000000000000000000", "Наличные", null, "RUR", PERSON_ID, null)),
                        AccountingApi.Account.Ref.class
                );
        assertThat(exchange.getBody()).usingRecursiveComparison().isEqualTo(
                new AccountingApi.Account.Ref("00000000000000000000")
        );
    }

    @Test
    public void modifyAccount() {
        ResponseEntity<AccountingApi.Account.Ref> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts/40817810000016123456",
                        HttpMethod.PUT, new HttpEntity<>(new AccountingApi.Account.Modify("Tinkoff", "044525974", "USD", Collections.singletonList("tinkoff_40817810000016123456"))),
                        AccountingApi.Account.Ref.class
                );
        assertThat(exchange.getBody()).usingRecursiveComparison().isEqualTo(
                new AccountingApi.Account.Ref("40817810000016123456")
        );

        assertThat(this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts/40817810000016123456",
                        HttpMethod.GET, new HttpEntity<>(null),
                        AccountingApi.Account.class
                ).getBody()
        ).usingRecursiveComparison().isEqualTo(
                new AccountingApi.Account("40817810000016123456", "Tinkoff", "044525974", "USD", PERSON_ID, Collections.singletonList("tinkoff_40817810000016123456"))
        );
    }

    @Test
    public void listOperations() {
        ResponseEntity<List<AccountingApi.AccountOperation>> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts/40817810108290123456/operations?from=2017-07-10&to=2017-07-10",
                        HttpMethod.GET, new HttpEntity<>(null),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertThat(exchange.getBody()).isNotEmpty();
    }

    @Test
    public void listHoldOperations() {
        ResponseEntity<List<AccountingApi.HoldOperation>> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts/40817810108290123456/holds?from=2020-02-27&to=2020-02-29",
                        HttpMethod.GET, new HttpEntity<>(null),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertThat(exchange.getBody()).isNotEmpty();
    }

    @Test
    public void getOperation() {
        final String operationId = "tx-operation-1";
        ResponseEntity<AccountingApi.AccountOperation> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/operations/{operationId}",
                        HttpMethod.GET, new HttpEntity<>(null),
                        AccountingApi.AccountOperation.class,
                        operationId
                );
        assertThat(exchange.getBody())
                .usingRecursiveComparison()
                .comparingOnlyFields("account", "date", "authorizationDate", "operationId", "operationType", "currency", "description")
                .isEqualTo(new AccountingApi.AccountOperation(
                "40817810108290123456",
                LocalDate.of(2018, 8, 1),
                null,
                null,
                "tx-operation-1",
                "-",
                3000d,
                "RUR",
                "перевод на ГПБ",
                null,
                null
        ));
    }

    @Test
    public void deleteOperation() {
        final String operationId = "will-be-removed";
        ResponseEntity<AccountingApi.AccountOperation> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/operations/{operationId}",
                        HttpMethod.DELETE, new HttpEntity<>(null),
                        AccountingApi.AccountOperation.class,
                        operationId
                );
        assertThat(exchange.getBody())
                .usingRecursiveComparison().isEqualTo(new AccountingApi.AccountOperation(
                        "40817810108290123456",
                        LocalDate.of(2017, 7, 10),
                        null,
                        null,
                        "will-be-removed",
                        "-",
                        10.20d,
                        "RUR",
                        "тестовая операция для удаления",
                        null,
                        781381038049753674L
                ));
        assertThat(this.restTemplate.withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts/40817810108290123456/operations?from=2017-07-01&to=2017-07-20",
                        HttpMethod.GET, new HttpEntity<>(null),
                        new ParameterizedTypeReference<List<AccountingApi.AccountOperation>>() {
                        }
                ).getBody()).isNotEmpty().doNotHave(new Condition<>() {
            @Override
            public boolean matches(AccountingApi.AccountOperation value) {
                return "will-be-removed".equalsIgnoreCase(value.operationId());
            }
        });
    }

    @Test
    public void createOperation() {
        final String accountNumber = "40817810108290123456";
        final LocalDate operationDate = LocalDate.of(2017, 7, 10);
        final String txref = "TXREF1";
        final String description = "Созданная вручную операция";
        final String currency = "RUR";
        final double amount = 5678.99d;
        final long categoryId = 781381038049753674L;
        ResponseEntity<AccountingApi.AccountOperation> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts/{accountNumber}/operations",
                        HttpMethod.POST, new HttpEntity<>(new AccountingApi.AccountOperation.Create(
                                operationDate,
                                null,
                                txref,
                                String.valueOf(OperationType.WITHDRAW.symbol()),
                                amount,
                                currency,
                                description,
                                null,
                                categoryId
                        )),
                        AccountingApi.AccountOperation.class,
                        accountNumber
                );
        final String operationId = Objects.requireNonNull(exchange.getBody()).operationId();
        assertThat(exchange.getBody()).usingRecursiveComparison().isEqualTo(new AccountingApi.AccountOperation(
                accountNumber,
                operationDate,
                null,
                txref,
                operationId,
                String.valueOf(OperationType.WITHDRAW.symbol()),
                amount,
                currency,
                description,
                null,
                categoryId
        ));

        assertThat(this.restTemplate.withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts/{accountNumber}/operations?from={from}&to={to}",
                        HttpMethod.GET, new HttpEntity<>(null),
                        new ParameterizedTypeReference<List<AccountingApi.AccountOperation>>() {
                        },
                        accountNumber,
                        operationDate.minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE),
                        operationDate.plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE)
                ).getBody())
                .isNotEmpty()
                .haveExactly(1, new Condition<>() {
                    @Override
                    public boolean matches(AccountingApi.AccountOperation value) {
                        return operationId.equalsIgnoreCase(value.operationId());
                    }
                });
    }

    @Test
    public void assignCategory() {
        final String operationId = "tx-operation-1";
        ResponseEntity<AccountingApi.AccountOperation> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/operations/{operationId}",
                        HttpMethod.PATCH, new HttpEntity<>(new OperationCategoryPatch(781381038049753674L), null),
                        new ParameterizedTypeReference<>() {
                        },
                        operationId
                );
        assertThat(Objects.requireNonNull(exchange.getBody()).categoryId()).isEqualTo(781381038049753674L);
    }

    @Test
    public void modifyComment() {
        final String operationId = "tx-operation-1";

        HashMap<String, Object> body = new HashMap<>();
        body.put("comment", "my comment");

        ResponseEntity<AccountingApi.AccountOperation> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/operations/{operationId}",
                        HttpMethod.PATCH, new HttpEntity<>(body, null),
                        new ParameterizedTypeReference<>() {
                        },
                        operationId
                );
        assertThat(Objects.requireNonNull(exchange.getBody()).comment()).isEqualTo("my comment");
    }

    private static class OperationCategoryPatch {
        private long categoryId;

        public OperationCategoryPatch() {
        }

        public OperationCategoryPatch(long categoryId) {
            this.categoryId = categoryId;
        }

        public long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(long categoryId) {
            this.categoryId = categoryId;
        }
    }

}
