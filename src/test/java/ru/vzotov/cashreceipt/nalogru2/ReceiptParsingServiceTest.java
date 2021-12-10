package ru.vzotov.cashreceipt.nalogru2;

import ru.vzotov.cashreceipt.domain.model.Check;
import ru.vzotov.cashreceipt.ReceiptFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class ReceiptParsingServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ReceiptParsingServiceTest.class);

    @Test
    public void parse001() throws IOException {
        Check check = new ReceiptFactory().createReceiptFromJson2("/nalogru2/check001.json");
        log.info("Parsed check {}", check);
        assertThat(
                LocalDateTime.of(2020, 8, 5, 21, 11)
        ).isEqualTo(check.dateTime());
    }

    @Test
    public void parse002() throws IOException {
        Check check = new ReceiptFactory().createReceiptFromJson2("/nalogru2/check002.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse003() throws IOException {
        Check check = new ReceiptFactory().createReceiptFromJson2("/nalogru2/check003.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse023() throws IOException {
        Check check = new ReceiptFactory().createReceiptFromJson2("/nalogru2/check023.json");
        log.info("Parsed check {}", check);
    }

    @Test
    public void parse024() throws IOException {
        Check check = new ReceiptFactory().createReceiptFromJson2("/nalogru2/check024.json");
        log.info("Parsed check {}", check);
    }

}
