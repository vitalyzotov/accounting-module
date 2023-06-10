package ru.vzotov.cashreceipt.nalogru;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vzotov.cashreceipt.ReceiptFactory;
import ru.vzotov.cashreceipt.domain.model.Receipt;
import ru.vzotov.person.domain.model.PersonId;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReceiptParsingServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ReceiptParsingServiceTest.class);

    private static final PersonId PERSON_ID = new PersonId("c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2");

    @Test
    public void parse003() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt003.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse006() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt006.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse007() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt007.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse008() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt008.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse009() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt009.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse010() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt010.json");
        log.info("Parsed receipt {}", receipt);
        assertThat(receipt.retailPlace().user().length())
                .as("user not trimmed").isEqualTo(14);
        assertThat(receipt.fiscalInfo().kktRegId().length())
                .as("kkt reg id not trimmed").isEqualTo(16);
    }

    @Test
    public void parse011() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt011.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse012() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt012.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse013() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt013.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse014() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt014.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse015() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt015.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse017() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt017.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse019() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt019.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse020() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt020.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse021() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt021.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse022() throws IOException {
        assertThatThrownBy(() -> new ReceiptFactory().createReceiptFromJson(PERSON_ID, "/receipt022.json"))
                .isInstanceOf(Exception.class);
    }

}
