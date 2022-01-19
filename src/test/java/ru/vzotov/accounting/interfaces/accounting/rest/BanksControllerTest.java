package ru.vzotov.accounting.interfaces.accounting.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BankDTO;
import ru.vzotov.accounting.test.AbstractControllerTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class BanksControllerTest extends AbstractControllerTest {

    @Test
    public void listBanks() {
        ResponseEntity<List<BankDTO>> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/banks",
                        HttpMethod.GET, new HttpEntity<>(null),
                        new ParameterizedTypeReference<List<BankDTO>>() {
                        }
                );
        assertThat(exchange.getBody()).isNotEmpty();
    }
}
