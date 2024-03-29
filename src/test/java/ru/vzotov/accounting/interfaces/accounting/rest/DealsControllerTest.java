package ru.vzotov.accounting.interfaces.accounting.rest;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.common.CommonApi;
import ru.vzotov.accounting.interfaces.purchases.PurchasesApi;
import ru.vzotov.accounting.test.AbstractControllerTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DealsControllerTest extends AbstractControllerTest {

    private static final Logger log = LoggerFactory.getLogger(DealsControllerTest.class);

    public static final String DEAL_DESCRIPTION = "description of my deal";

    @Test
    public void listDeals() {
        ResponseEntity<List<AccountingApi.Deal>> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/deals?from=2017-07-10&to=2017-07-10",
                        HttpMethod.GET, new HttpEntity<>(null),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).isNotEmpty();
    }

    @Test
    public void getDeal() {
        ResponseEntity<AccountingApi.Deal> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/deals/deal-1",
                        HttpMethod.GET, new HttpEntity<>(null),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).hasFieldOrPropertyWithValue("dealId", "deal-1");
        assertThat(exchange.getBody()).hasFieldOrPropertyWithValue("cardOperations",
                Collections.singletonList(new AccountingApi.OperationId("test-operation-1")));
    }

    @Test
    public void getDealWithExpansion() {
        ResponseEntity<AccountingApi.Deal> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/deals/deal-1?expand=card_operations",
                        HttpMethod.GET, new HttpEntity<>(null),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).hasFieldOrPropertyWithValue("dealId", "deal-1");
        assertThat(exchange.getBody()).hasFieldOrPropertyWithValue("cardOperations",
                Collections.singletonList(new AccountingApi.CardOperation(
                        "test-operation-1",
                        "555957++++++1234",
                        new AccountingApi.PosTerminal(
                                "10705017",
                                "RUS",
                                "MOSCOW",
                                "1 YA T",
                                "ROSTELECOM"
                        ),
                        LocalDate.of(2018, 7, 10),
                        LocalDate.of(2018, 7, 7),
                        new CommonApi.Money(50000, "RUR"),
                        null,
                        "4812"
                )));
    }

    @Test
    public void deleteDeal() {
        ResponseEntity<List<AccountingApi.Deal>> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/deals/deal-for-remove",
                        HttpMethod.DELETE, new HttpEntity<>(null),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).hasSize(0);
    }

    @Test
    public void deleteDealWithReceipt() {
        final ResponseEntity<List<AccountingApi.Deal>> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/deals/deal-for-remove-3?receipt=true",
                        HttpMethod.DELETE, new HttpEntity<>(null),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).hasSize(0);

        final ResponseEntity<AccountingApi.QRCode> result = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/qr/receipt_of_deal_to_remove",
                        HttpMethod.GET, new HttpEntity<>(null),
                        new ParameterizedTypeReference<>() {
                        }
                );
        log.info("qr {}", result.getBody());
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void createDeal() {
        ResponseEntity<AccountingApi.Deal> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/deals",
                        HttpMethod.POST, new HttpEntity<>(new AccountingApi.Deal(
                                "deal-created-via-api",
                                PERSON_ID,
                                LocalDate.of(2021, 11, 21),
                                new CommonApi.Money(-3000, "RUR"), DEAL_DESCRIPTION, "comment of deal",
                                781381038049753674L,
                                Collections.singletonList(new AccountingApi.ReceiptId("receipt-6380ff16f05e")),
                                Collections.singletonList(new AccountingApi.OperationId("deal-operation-1")),
                                Collections.emptyList(),
                                Collections.singletonList(new PurchasesApi.PurchaseId("purchase-3000-1"))
                        )),
                        AccountingApi.Deal.class
                );
        assertThat(exchange.getBody()).hasFieldOrPropertyWithValue("description", DEAL_DESCRIPTION);
    }

    @Test
    public void modifyDeal() {
        ResponseEntity<Void> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/deals/deal-for-modification",
                        HttpMethod.PUT, new HttpEntity<>(new AccountingApi.Deal(
                                "deal-for-modification",
                                PERSON_ID,
                                LocalDate.of(2021, 11, 21),
                                new CommonApi.Money(-5000, "RUR"), "modified description", "modified comment",
                                781381038049753674L,
                                Collections.singletonList(new AccountingApi.ReceiptId("receipt-923fe9456109")),
                                Collections.singletonList(new AccountingApi.OperationId("deal-operation-3")),
                                Collections.emptyList(),
                                Collections.singletonList(new PurchasesApi.PurchaseId("purchase-5000-1"))
                        )),
                        Void.class
                );

        assertThat(exchange.getStatusCode())
                .isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).isNull();
    }

    @Test
    public void patchDeals() {
        Map<String, Object> data = new HashMap<>();
        data.put("deals", Arrays.asList("cf7cb2f0-ea2a-4ba4-8732-43544af8bbc8", "c68ad6eb-86af-47f3-8165-a09a9945093f"));

        ResponseEntity<AccountingApi.Deal> exchange = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/deals",
                        HttpMethod.PATCH, new HttpEntity<>(data),
                        AccountingApi.Deal.class
                );

        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).isNotNull();
        assertThat(exchange.getBody().operations()).extracting(AccountingApi.OperationRef::operationId)
                .containsExactlyInAnyOrder("98364d73-c42b-4e5b-93da-a0a6d6018a3b", "9102dfe0-a0c8-4a83-8283-d1d487a4695c");
        assertThat(exchange.getBody().receipts()).extracting(AccountingApi.ReceiptRef::receiptId)
                .containsExactlyInAnyOrder("0a735210-65e5-4b1d-abf3-7a36f707b050", "f4668455-e756-4af8-89dd-d90a7ed6ff15");
        assertThat(exchange.getBody().amount().amount()).isEqualTo(-4999 - 2999);
        assertThat(exchange.getBody()).hasFieldOrPropertyWithValue("comment", "comment a09a9945093f");
        assertThat(exchange.getBody()).hasFieldOrPropertyWithValue("category", 781381038049753674L);
    }
}
