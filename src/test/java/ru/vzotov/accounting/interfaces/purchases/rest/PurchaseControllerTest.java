package ru.vzotov.accounting.interfaces.purchases.rest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.interfaces.common.dto.MoneyDTO;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;
import ru.vzotov.accounting.interfaces.purchases.rest.dto.PurchaseCreateRequest;
import ru.vzotov.accounting.interfaces.purchases.rest.dto.PurchaseData;
import ru.vzotov.accounting.interfaces.purchases.rest.dto.PurchaseModifyRequest;
import ru.vzotov.accounting.interfaces.purchases.rest.dto.PurchaseStoreResponse;
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
        PurchaseDTO body = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .getForObject("/accounting/purchases/20180616135500_2ee_56e0ad53f97dc8ac9ddbc7fa37052b44", PurchaseDTO.class);
        assertThat(body).isNotNull();
    }

    @Test
    public void searchPurchase() {
        PurchaseDTO[] body = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .getForObject("/accounting/purchases?from=2018-06-16T13:55&to=2018-06-16T13:56", PurchaseDTO[].class);
        Assertions.assertThat(body).
                isNotNull().
                isNotEmpty().
                hasAtLeastOneElementOfType(PurchaseDTO.class);
    }

    @Test
    public void newPurchase() {
        PurchaseData data = new PurchaseData();
        data.setName("Позиция 3");
        data.setDateTime(LocalDateTime.of(2018, Month.JUNE, 16, 13, 55, 10));
        data.setPrice(new MoneyDTO(60000, "RUR"));
        data.setQuantity(10d);

        PurchaseCreateRequest request = new PurchaseCreateRequest("deal-2", Collections.singletonList(data));

        PurchaseStoreResponse response = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .postForObject("/accounting/purchases", request, PurchaseStoreResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.getPurchaseId()).isNotEmpty();
        assertThat(response.getPurchaseId().get(0)).isNotEmpty();
    }

    @Test
    public void modifyPurchase() {
        PurchaseModifyRequest request = new PurchaseModifyRequest();
        request.setReceiptId("20180616135500_65624_8710000100313204_110992_2128735201_1");
        request.setName("Позиция с другим названием");
        request.setCategoryId("id-12345678901234567890");

        ResponseEntity<PurchaseStoreResponse> response = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/purchases/20180616135500_2ee_56e0ad53f97dc8ac9ddbc7fa37052b44", HttpMethod.PUT,
                        new HttpEntity<>(request), PurchaseStoreResponse.class
                );
        assertThat(response).isNotNull();
        assertThat(response.getBody().getPurchaseId()).isNotEmpty().isEqualTo(Collections.singletonList("20180616135500_2ee_56e0ad53f97dc8ac9ddbc7fa37052b44"));
    }

}
