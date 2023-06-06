package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.cashreceipt.ReceiptFactory;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategory;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryRepository;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.domain.model.Receipt;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.cashreceipt.domain.model.ReceiptRepository;
import ru.vzotov.person.domain.model.PersonId;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class ReceiptRepositoryJpaTest {

    private static final Logger log = LoggerFactory.getLogger(ReceiptRepositoryJpaTest.class);

    private static final String PERSON_ID = "c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2";

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private PurchaseCategoryRepository categoryRepository;

    @Test
    public void find() {
        Receipt receipt = receiptRepository.find(new ReceiptId("20180616135500_65624_8710000100313204_110992_2128735201_1"));
        log.info("Receipt loaded {}", receipt);
        assertThat(receipt).isNotNull();
    }

    @Test
    public void assignItemToCategory() {
        Receipt receipt = receiptRepository.find(new ReceiptId("20180616135500_65624_8710000100313204_110992_2128735201_1"));
        log.info("Receipt loaded {}", receipt);
        assertThat(receipt).isNotNull();

        PurchaseCategory cat = categoryRepository.findByName(new PersonId(PERSON_ID), "Пакеты");
        log.info("Category loaded {}", cat);
        assertThat(cat).isNotNull();

        receipt.assignCategoryToItem(0, cat);

        receiptRepository.store(receipt);
        log.info("Receipt persisted {}", receipt);
    }

    @Test
    public void store() {
        Receipt receipt = new ReceiptFactory().createReceiptSimple(new PersonId(PERSON_ID));
        receiptRepository.store(receipt);
    }

    @Test
    public void storeLongUser() {
        Receipt receipt = new ReceiptFactory().createReceiptWithLongUser(new PersonId(PERSON_ID));
        receiptRepository.store(receipt);
    }

    @Test
    public void findByQRCodeData() {
        Receipt notFound = receiptRepository.findByQRCodeData(new QRCodeData("t=20180614T1641&s=566.92&fn=8710000100312991&i=21128&fp=2663320648&n=1"));
        assertThat(notFound).isNull();

        Receipt receipt = receiptRepository.findByQRCodeData(new QRCodeData("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1"));
        log.info("Receipt loaded {}", receipt);
        assertThat(receipt).isNotNull();
    }

    @Test
    public void findByQRCodeDataIgnoreSeconds() {
        Receipt receipt = receiptRepository.findByQRCodeData(new QRCodeData("t=20210615T131415&s=150.00&fn=1234567891234567&i=15141&fp=1234567891&n=1"));
        assertThat(receipt).isNotNull();
    }

}
