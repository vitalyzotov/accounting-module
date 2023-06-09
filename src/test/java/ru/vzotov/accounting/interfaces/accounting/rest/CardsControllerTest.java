package ru.vzotov.accounting.interfaces.accounting.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.test.AbstractControllerTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class CardsControllerTest extends AbstractControllerTest {

    @Test
    public void listCards() {
        ResponseEntity<List<AccountingApi.Card>> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/cards",
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<List<AccountingApi.Card>>() {
                }
        );
        assertThat(exchange.getBody()).isNotEmpty();
    }
}
