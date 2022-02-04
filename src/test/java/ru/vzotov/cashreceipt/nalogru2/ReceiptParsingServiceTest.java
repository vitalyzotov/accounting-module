package ru.vzotov.cashreceipt.nalogru2;

import ru.vzotov.cashreceipt.domain.model.Receipt;
import ru.vzotov.cashreceipt.ReceiptFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vzotov.person.domain.model.PersonId;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class ReceiptParsingServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ReceiptParsingServiceTest.class);

    private static final PersonId PERSON_ID = new PersonId("c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2");

    @Test
    public void parse001() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson2(PERSON_ID, "/nalogru2/receipt001.json");
        log.info("Parsed receipt {}", receipt);
        assertThat(
                LocalDateTime.of(2020, 8, 5, 21, 11)
        ).isEqualTo(receipt.dateTime());
    }

    @Test
    public void parse002() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson2(PERSON_ID, "/nalogru2/receipt002.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse003() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson2(PERSON_ID, "/nalogru2/receipt003.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse023() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson2(PERSON_ID, "/nalogru2/receipt023.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse024() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson2(PERSON_ID, "/nalogru2/receipt024.json");
        log.info("Parsed receipt {}", receipt);
    }

}
