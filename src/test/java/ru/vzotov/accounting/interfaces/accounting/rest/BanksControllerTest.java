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
public class BanksControllerTest extends AbstractControllerTest {

    @Test
    public void listBanks() {
        ResponseEntity<List<AccountingApi.Bank>> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/banks",
                        HttpMethod.GET, new HttpEntity<>(null),
                        new ParameterizedTypeReference<>() {
                        }
                );
        assertThat(exchange.getBody()).isNotEmpty();
    }
}
