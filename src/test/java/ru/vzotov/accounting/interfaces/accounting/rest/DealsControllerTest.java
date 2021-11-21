package ru.vzotov.accounting.interfaces.accounting.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.DealDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.MoneyDTO;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DealsControllerTest {
    public static final String DEAL_DESCRIPTION = "description of my deal";
    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        // PATCH support
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    public void listDeals() {
        ResponseEntity<List<DealDTO>> exchange = this.restTemplate.exchange(
                "/accounting/deals?from=2017-07-10&to=2017-07-10",
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<List<DealDTO>>() {
                }
        );
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).isNotEmpty();
    }

    @Test
    public void getDeal() {
        ResponseEntity<DealDTO> exchange = this.restTemplate.exchange(
                "/accounting/deals/deal-1",
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<DealDTO>() {
                }
        );
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).hasFieldOrPropertyWithValue("dealId", "deal-1");
    }

    @Test
    public void deleteDeal() {
        ResponseEntity<DealDTO> exchange = this.restTemplate.exchange(
                "/accounting/deals/deal-for-remove",
                HttpMethod.DELETE, new HttpEntity<>(null),
                new ParameterizedTypeReference<DealDTO>() {
                }
        );
        assertThat(exchange.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(exchange.getBody()).hasFieldOrPropertyWithValue("dealId", "deal-for-remove");
    }

    @Test
    public void createDeal() {
        ResponseEntity<DealDTO> exchange = this.restTemplate.exchange(
                "/accounting/deals",
                HttpMethod.POST, new HttpEntity<>(new DealDTO(
                        "deal-created-via-api", LocalDate.of(2021, 11, 21),
                        new MoneyDTO(-3000, "RUR"), DEAL_DESCRIPTION, "comment of deal",
                        781381038049753674L,
                        Collections.singletonList("20180717152900_365000_8712000101115142_19645_3757443940_1"),
                        Collections.singletonList("deal-operation-1"))),
                DealDTO.class
        );
        assertThat(exchange.getBody()).hasFieldOrPropertyWithValue("description", DEAL_DESCRIPTION);
    }

    @Test
    public void modifyDeal() {
        ResponseEntity<DealDTO> exchange = this.restTemplate.exchange(
                "/accounting/deals/deal-for-modification",
                HttpMethod.PUT, new HttpEntity<>(new DealDTO(
                        "deal-for-modification", LocalDate.of(2021, 11, 21),
                        new MoneyDTO(-5000, "RUR"), "modified description", "modified comment",
                        781381038049753674L,
                        Collections.singletonList("20180724145300_10650_9281000100225396_2908_4063563774_1"),
                        Collections.singletonList("deal-operation-3"))),
                DealDTO.class
        );

        assertThat(exchange.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        final DealDTO result = exchange.getBody();
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("description", "modified description")
                .hasFieldOrPropertyWithValue("comment", "modified comment")
                .hasFieldOrPropertyWithValue("category", 781381038049753674L);

        assertThat(result.getOperations()).contains("deal-operation-3");
    }
}
