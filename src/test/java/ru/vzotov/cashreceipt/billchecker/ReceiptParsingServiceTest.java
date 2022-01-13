package ru.vzotov.cashreceipt.billchecker;

import ru.vzotov.cashreceipt.domain.model.Receipt;
import ru.vzotov.cashreceipt.ReceiptFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@RunWith(JUnit4.class)
public class ReceiptParsingServiceTest {
    private static final Logger log = LoggerFactory.getLogger(ReceiptParsingServiceTest.class);

    @Test
    public void parseMagnit() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromBillchecker("/receipt002.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parseSemeiny() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromBillchecker("/receipt004.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parseMailru() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromBillchecker("/receipt005.json");
        log.info("Parsed receipt {}", receipt);
    }

}
