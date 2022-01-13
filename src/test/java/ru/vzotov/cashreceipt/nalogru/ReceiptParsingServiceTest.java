package ru.vzotov.cashreceipt.nalogru;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vzotov.cashreceipt.ReceiptFactory;
import ru.vzotov.cashreceipt.domain.model.Receipt;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(JUnit4.class)
public class ReceiptParsingServiceTest {

    private static final Logger log = LoggerFactory.getLogger(ReceiptParsingServiceTest.class);

    @Test
    public void parse003() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson("/receipt003.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse006() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson("/receipt006.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse007() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson("/receipt007.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse008() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson("/receipt008.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse009() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson("/receipt009.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse010() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson("/receipt010.json");
        log.info("Parsed receipt {}", receipt);

        Assert.assertEquals("user not trimmed", 14, receipt.retailPlace().user().length());
        Assert.assertEquals("kkt reg id not trimmed", 16, receipt.fiscalInfo().kktRegId().length());

    }

    @Test
    public void parse011() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson("/receipt011.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse012() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson("/receipt012.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse013() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson("/receipt013.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse014() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson("/receipt014.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse015() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson("/receipt015.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse017() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson("/receipt017.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse019() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson("/receipt019.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse020() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson("/receipt020.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse021() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromJson("/receipt021.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parse022() throws IOException {
        assertThatThrownBy(() -> new ReceiptFactory().createReceiptFromJson("/receipt022.json"))
                .isInstanceOf(Exception.class);
    }

}
