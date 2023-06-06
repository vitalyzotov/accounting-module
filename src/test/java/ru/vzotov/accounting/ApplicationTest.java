package ru.vzotov.accounting;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationTest {
    private static final Logger log = LoggerFactory.getLogger(ApplicationTest.class);

    @Test
    public void contextLoads() {
        log.info("Context is loaded");
    }
}
