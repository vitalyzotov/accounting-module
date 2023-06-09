package ru.vzotov.accounting.interfaces.accounting.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.test.AbstractControllerTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class ConfigControllerTest extends AbstractControllerTest {

    public static final String ACCOUNTING_CONFIG_NALOG_AUTH = "/accounting/config/nalog-auth";

    @Test
    public void testBadRequest() {
        HttpEntity<Map<String, String>> request = new HttpEntity<>(new HashMap<>());
        ResponseEntity<Void> response = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .exchange(ACCOUNTING_CONFIG_NALOG_AUTH, HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testValidRequest() {
        HttpEntity<ConfigController.NalogPreAuthRequest> request = new HttpEntity<>(new ConfigController.NalogPreAuthRequest("test_session", "test_refresh"));
        ResponseEntity<Void> response = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .exchange(ACCOUNTING_CONFIG_NALOG_AUTH, HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
