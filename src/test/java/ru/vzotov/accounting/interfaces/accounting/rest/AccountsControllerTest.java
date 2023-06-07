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
import ru.vzotov.accounting.interfaces.accounting.facade.dto.AccountDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.AccountOperationDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.HoldOperationDTO;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.AccountCreateRequest;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.AccountModifyRequest;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.AccountStoreResponse;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.OperationCreateRequest;
import ru.vzotov.accounting.test.AbstractControllerTest;
import ru.vzotov.banking.domain.model.OperationType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class AccountsControllerTest extends AbstractControllerTest {

    @Test
    public void getAccount() {
        ResponseEntity<AccountDTO> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts/40817810108290123456",
                        HttpMethod.GET, new HttpEntity<>(null),
                        AccountDTO.class
                );
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody())
                .isNotNull()
                .hasFieldOrPropertyWithValue("number", "40817810108290123456");
    }

    @Test
    public void listAccounts() {
        ResponseEntity<List<AccountDTO>> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts",
                        HttpMethod.GET, new HttpEntity<>(null),
                        new ParameterizedTypeReference<List<AccountDTO>>() {
                        }
                );
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).isNotEmpty();
    }

    @Test
    public void createAccount() {
        ResponseEntity<AccountStoreResponse> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts",
                        HttpMethod.POST, new HttpEntity<>(new AccountCreateRequest("00000000000000000000", "Наличные", null, "RUR", PERSON_ID, null)),
                        AccountStoreResponse.class
                );
        assertThat(exchange.getBody()).usingRecursiveComparison().isEqualTo(
                new AccountStoreResponse("00000000000000000000")
        );
    }

    @Test
    public void modifyAccount() {
        ResponseEntity<AccountStoreResponse> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts/40817810000016123456",
                        HttpMethod.PUT, new HttpEntity<>(new AccountModifyRequest("Tinkoff", "044525974", "USD", Collections.singletonList("tinkoff_40817810000016123456"))),
                        AccountStoreResponse.class
                );
        assertThat(exchange.getBody()).usingRecursiveComparison().isEqualTo(
                new AccountStoreResponse("40817810000016123456")
        );

        assertThat(this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts/40817810000016123456",
                        HttpMethod.GET, new HttpEntity<>(null),
                        AccountDTO.class
                ).getBody()
        ).usingRecursiveComparison().isEqualTo(
                new AccountDTO("40817810000016123456", "Tinkoff", "044525974", "USD", PERSON_ID, Collections.singletonList("tinkoff_40817810000016123456"))
        );
    }

    @Test
    public void listOperations() {
        ResponseEntity<List<AccountOperationDTO>> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts/40817810108290123456/operations?from=2017-07-10&to=2017-07-10",
                        HttpMethod.GET, new HttpEntity<>(null),
                        new ParameterizedTypeReference<List<AccountOperationDTO>>() {
                        }
                );
        assertThat(exchange.getBody()).isNotEmpty();
    }

    @Test
    public void listHoldOperations() {
        ResponseEntity<List<HoldOperationDTO>> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts/40817810108290123456/holds?from=2020-02-27&to=2020-02-29",
                        HttpMethod.GET, new HttpEntity<>(null),
                        new ParameterizedTypeReference<List<HoldOperationDTO>>() {
                        }
                );
        assertThat(exchange.getBody()).isNotEmpty();
    }

    @Test
    public void getOperation() {
        final String operationId = "tx-operation-1";
        ResponseEntity<AccountOperationDTO> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/operations/{operationId}",
                        HttpMethod.GET, new HttpEntity<>(null),
                        AccountOperationDTO.class,
                        operationId
                );
        assertThat(exchange.getBody()).isEqualToComparingOnlyGivenFields(new AccountOperationDTO(
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
        ), "account", "date", "authorizationDate", "operationId", "operationType", "currency", "description");
    }

    @Test
    public void deleteOperation() {
        final String operationId = "will-be-removed";
        ResponseEntity<AccountOperationDTO> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/operations/{operationId}",
                        HttpMethod.DELETE, new HttpEntity<>(null),
                        AccountOperationDTO.class,
                        operationId
                );
        assertThat(exchange.getBody())
                .usingRecursiveComparison().isEqualTo(new AccountOperationDTO(
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
                        new ParameterizedTypeReference<List<AccountOperationDTO>>() {
                        }
                ).getBody()).isNotEmpty().doNotHave(new Condition<AccountOperationDTO>() {
            @Override
            public boolean matches(AccountOperationDTO value) {
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
        ResponseEntity<AccountOperationDTO> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/accounts/{accountNumber}/operations",
                        HttpMethod.POST, new HttpEntity<>(new OperationCreateRequest(
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
                        AccountOperationDTO.class,
                        accountNumber
                );
        final String operationId = exchange.getBody().operationId();
        assertThat(exchange.getBody()).usingRecursiveComparison().isEqualTo(new AccountOperationDTO(
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
                        new ParameterizedTypeReference<List<AccountOperationDTO>>() {
                        },
                        accountNumber,
                        operationDate.minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE),
                        operationDate.plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE)
                ).getBody())
                .isNotEmpty()
                .haveExactly(1, new Condition<AccountOperationDTO>() {
                    @Override
                    public boolean matches(AccountOperationDTO value) {
                        return operationId.equalsIgnoreCase(value.operationId());
                    }
                });
    }

    @Test
    public void assignCategory() {
        final String operationId = "tx-operation-1";
        ResponseEntity<AccountOperationDTO> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/operations/{operationId}",
                        HttpMethod.PATCH, new HttpEntity<>(new OperationCategoryPatch(781381038049753674L), null),
                        new ParameterizedTypeReference<AccountOperationDTO>() {
                        },
                        operationId
                );
        assertThat(exchange.getBody().categoryId()).isEqualTo(781381038049753674L);
    }

    @Test
    public void modifyComment() {
        final String operationId = "tx-operation-1";

        HashMap<String, Object> body = new HashMap<>();
        body.put("comment", "my comment");

        ResponseEntity<AccountOperationDTO> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/operations/{operationId}",
                        HttpMethod.PATCH, new HttpEntity<>(body, null),
                        new ParameterizedTypeReference<AccountOperationDTO>() {
                        },
                        operationId
                );
        assertThat(exchange.getBody().comment()).isEqualTo("my comment");
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
