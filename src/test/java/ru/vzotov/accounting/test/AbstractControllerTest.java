package ru.vzotov.accounting.test;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

public abstract class AbstractControllerTest {

    protected static final String PERSON_ID = "c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2";
    protected static final String USER = "user_" + PERSON_ID;
    protected static final String PASSWORD = "password";

    @Autowired
    protected TestRestTemplate restTemplate;

    @Before
    public void setup() {
        // PATCH support
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

}
