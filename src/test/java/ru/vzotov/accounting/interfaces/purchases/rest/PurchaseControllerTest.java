package ru.vzotov.accounting.interfaces.purchases.rest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.interfaces.common.CommonApi;
import ru.vzotov.accounting.interfaces.purchases.PurchasesApi;
import ru.vzotov.accounting.test.AbstractControllerTest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class PurchaseControllerTest extends AbstractControllerTest {

    @Test
    public void getPurchaseById() {
        PurchasesApi.Purchase body = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .getForObject("/accounting/purchases/20180616135500_2ee_56e0ad53f97dc8ac9ddbc7fa37052b44", PurchasesApi.Purchase.class);
        assertThat(body).isNotNull();
    }

    @Test
    public void searchPurchase() {
        PurchasesApi.Purchase[] body = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .getForObject("/accounting/purchases?from=2018-06-16T13:55&to=2018-06-16T13:56", PurchasesApi.Purchase[].class);
        Assertions.assertThat(body).
                isNotNull().
                isNotEmpty().
                hasAtLeastOneElementOfType(PurchasesApi.Purchase.class);
    }

    @Test
    public void newPurchase() {
        PurchasesApi.Purchase.Data data = new PurchasesApi.Purchase.Data(
                "Позиция 3",
                LocalDateTime.of(2018, Month.JUNE, 16, 13, 55, 10),
                new CommonApi.Money(60000, "RUR"),
                10d
        );

        PurchasesApi.Purchase.Create request = new PurchasesApi.Purchase.Create("deal-2", Collections.singletonList(data));

        PurchasesApi.Purchase.Refs response = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .postForObject("/accounting/purchases", request, PurchasesApi.Purchase.Refs.class);
        assertThat(response).isNotNull();
        assertThat(response.purchaseId()).isNotEmpty();
        assertThat(response.purchaseId().get(0)).isNotEmpty();
    }

    @Test
    public void modifyPurchase() {
        PurchasesApi.Purchase.Data request = new PurchasesApi.Purchase.Data(
                "Позиция с другим названием",
                null,
                null,
                null,
                "20180616135500_65624_8710000100313204_110992_2128735201_1",
                "id-12345678901234567890"
        );

        ResponseEntity<PurchasesApi.Purchase.Refs> response = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/purchases/20180616135500_2ee_56e0ad53f97dc8ac9ddbc7fa37052b44", HttpMethod.PUT,
                        new HttpEntity<>(request), PurchasesApi.Purchase.Refs.class
                );
        assertThat(response).isNotNull()
                .extracting(HttpEntity::getBody).isNotNull()
                .extracting(PurchasesApi.Purchase.Refs::purchaseId).asList().isNotEmpty()
                .isEqualTo(Collections.singletonList("20180616135500_2ee_56e0ad53f97dc8ac9ddbc7fa37052b44"));
    }

}
