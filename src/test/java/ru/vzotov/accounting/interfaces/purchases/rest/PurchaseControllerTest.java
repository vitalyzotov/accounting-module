package ru.vzotov.accounting.interfaces.purchases.rest;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.interfaces.common.dto.MoneyDTO;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;
import ru.vzotov.accounting.interfaces.purchases.rest.dto.PurchaseCreateRequest;
import ru.vzotov.accounting.interfaces.purchases.rest.dto.PurchaseModifyRequest;
import ru.vzotov.accounting.interfaces.purchases.rest.dto.PurchaseStoreResponse;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class PurchaseControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getPurchaseById() {
        PurchaseDTO body = this.restTemplate.getForObject("/accounting/purchases/20180616135500_2ee_56e0ad53f97dc8ac9ddbc7fa37052b44", PurchaseDTO.class);
        assertThat(body).isNotNull();
    }

    @Test
    public void searchPurchase() {
        PurchaseDTO[] body = this.restTemplate.getForObject("/accounting/purchases?from=2018-06-16T13:55&to=2018-06-16T13:56", PurchaseDTO[].class);
        Assertions.assertThat(body).
                isNotNull().
                isNotEmpty().
                hasAtLeastOneElementOfType(PurchaseDTO.class);
    }

    @Test
    public void newPurchase() {
        PurchaseCreateRequest request = new PurchaseCreateRequest("deal-2");
        request.setName("Позиция 3");
        request.setDateTime(LocalDateTime.of(2018, Month.JUNE, 16, 13, 55, 10));
        request.setPrice(new MoneyDTO(60000, "RUR"));
        request.setQuantity(10d);
        PurchaseStoreResponse response = this.restTemplate.postForObject("/accounting/purchases", request, PurchaseStoreResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.getPurchaseId()).isNotEmpty();
    }

    @Test
    public void modifyPurchase() {
        PurchaseModifyRequest request = new PurchaseModifyRequest();
        request.setCheckId("20180616135500_65624_8710000100313204_110992_2128735201_1");
        request.setName("Позиция с другим названием");
        request.setCategoryId("id-12345678901234567890");

        ResponseEntity<PurchaseStoreResponse> response = this.restTemplate.exchange(
                "/accounting/purchases/20180616135500_2ee_56e0ad53f97dc8ac9ddbc7fa37052b44", HttpMethod.PUT,
                new HttpEntity<>(request), PurchaseStoreResponse.class);
        assertThat(response).isNotNull();
        assertThat(response.getBody().getPurchaseId()).isNotEmpty().isEqualTo("20180616135500_2ee_56e0ad53f97dc8ac9ddbc7fa37052b44");
    }

}
