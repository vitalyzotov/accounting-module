package ru.vzotov.cashreceipt.billchecker;

import ru.vzotov.cashreceipt.domain.model.Receipt;
import ru.vzotov.cashreceipt.ReceiptFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vzotov.person.domain.model.PersonId;

import java.io.IOException;

@RunWith(JUnit4.class)
public class ReceiptParsingServiceTest {
    private static final Logger log = LoggerFactory.getLogger(ReceiptParsingServiceTest.class);

    private static final PersonId PERSON_ID = new PersonId("c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2");

    @Test
    public void parseMagnit() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromBillchecker(PERSON_ID, "/receipt002.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parseSemeiny() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromBillchecker(PERSON_ID, "/receipt004.json");
        log.info("Parsed receipt {}", receipt);
    }

    @Test
    public void parseMailru() throws IOException {
        Receipt receipt = new ReceiptFactory().createReceiptFromBillchecker(PERSON_ID, "/receipt005.json");
        log.info("Parsed receipt {}", receipt);
    }

}
