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
public class MccControllerTest extends AbstractControllerTest {

    @Test
    public void listGroups() {
        ResponseEntity<List<AccountingApi.MccGroup>> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/mcc/groups",
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(exchange.getBody()).isNotEmpty().hasSize(20);
    }

    @Test
    public void getGroup() {
        ResponseEntity<AccountingApi.MccGroup> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/mcc/groups/{groupId}",
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<>() {
                },
                "c2711c3f-ba04-420f-8767-6055ffc28449"
        );
        assertThat(exchange.getBody())
                .usingRecursiveComparison()
                .isEqualTo(new AccountingApi.MccGroup("c2711c3f-ba04-420f-8767-6055ffc28449", "Автомобили и транспортные средства"));
    }

    @Test
    public void listAllDetails() {
        ResponseEntity<List<AccountingApi.MccDetails>> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/mcc/details",
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(exchange.getBody()).isNotEmpty().hasSize(1021);
    }

    @Test
    public void listGroupDetails() {
        ResponseEntity<List<AccountingApi.MccDetails>> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/mcc/details?group={group}",
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<>() {
                },
                "af7853df-3104-433a-86a3-5950273a7380"
        );
        assertThat(exchange.getBody()).isNotEmpty().hasSize(289);
    }

    @Test
    public void getDetailsByCode() {
        ResponseEntity<AccountingApi.MccDetails> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/mcc/details/{code}",
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<>() {
                },
                "0780"
        );
        assertThat(exchange.getBody())
                .usingRecursiveComparison()
                .isEqualTo(new AccountingApi.MccDetails("0780", "Услуги садоводства и ландшафтного дизайна", "4fce2602-d6d3-4bec-a7ff-df70cb767c2d"));
    }

    @Test
    public void getDetailsByCodeWithoutGroup() {
        ResponseEntity<AccountingApi.MccDetails> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/mcc/details/{code}",
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<>() {
                },
                "9701"
        );
        assertThat(exchange.getBody())
                .usingRecursiveComparison()
                .isEqualTo(new AccountingApi.MccDetails("9701", "Служба проверки учетных данных Visa (только VISA)", null));
    }

}
