package ru.vzotov.accounting.interfaces.accounting.rest;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.CardDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class CardsControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void listCards() {
        ResponseEntity<List<CardDTO>> exchange = this.restTemplate.exchange(
                "/accounting/cards",
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<List<CardDTO>>() {
                }
        );
        assertThat(exchange.getBody()).isNotEmpty();
    }
}
