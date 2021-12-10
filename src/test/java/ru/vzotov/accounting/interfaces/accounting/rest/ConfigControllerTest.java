package ru.vzotov.accounting.interfaces.accounting.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.NalogPreAuthRequest;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class ConfigControllerTest {

    public static final String ACCOUNTING_CONFIG_NALOG_AUTH = "/accounting/config/nalog-auth";
    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    public void testBadRequest() {
        HttpEntity<NalogPreAuthRequest> request = new HttpEntity<>(new NalogPreAuthRequest());
        ResponseEntity<Void> response = this.restTemplate.exchange(ACCOUNTING_CONFIG_NALOG_AUTH, HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testValidRequest() {
        HttpEntity<NalogPreAuthRequest> request = new HttpEntity<>(new NalogPreAuthRequest("test_session", "test_refresh"));
        ResponseEntity<Void> response = this.restTemplate.exchange(ACCOUNTING_CONFIG_NALOG_AUTH, HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
