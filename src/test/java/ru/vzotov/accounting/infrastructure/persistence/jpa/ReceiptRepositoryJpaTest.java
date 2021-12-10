package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.cashreceipt.ReceiptFactory;
import ru.vzotov.cashreceipt.domain.model.Check;
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategory;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryRepository;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.domain.model.ReceiptRepository;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class ReceiptRepositoryJpaTest {

    private static final Logger log = LoggerFactory.getLogger(ReceiptRepositoryJpaTest.class);

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private PurchaseCategoryRepository categoryRepository;

    @Test
    public void find() {
        Check receipt = receiptRepository.find(new CheckId("20180616135500_65624_8710000100313204_110992_2128735201_1"));
        log.info("Receipt loaded {}", receipt);
        Assert.assertNotNull(receipt);
    }

    @Test
    public void assignItemToCategory() {
        Check receipt = receiptRepository.find(new CheckId("20180616135500_65624_8710000100313204_110992_2128735201_1"));
        log.info("Receipt loaded {}", receipt);
        Assert.assertNotNull(receipt);

        PurchaseCategory cat = categoryRepository.findByName("Пакеты");
        log.info("Category loaded {}", cat);
        Assert.assertNotNull(cat);

        receipt.assignCategoryToItem(0, cat);

        receiptRepository.store(receipt);
        log.info("Receipt persisted {}", receipt);
    }

    @Test
    public void store() {
        Check receipt = new ReceiptFactory().createCheckSimple();
        receiptRepository.store(receipt);
    }

    @Test
    public void storeLongUser() {
        Check receipt = new ReceiptFactory().createCheckWithLongUser();
        receiptRepository.store(receipt);
    }

    @Test
    public void findByQRCodeData() {
        Check notFound = receiptRepository.findByQRCodeData(new QRCodeData("t=20180614T1641&s=566.92&fn=8710000100312991&i=21128&fp=2663320648&n=1"));
        Assert.assertNull(notFound);

        Check receipt = receiptRepository.findByQRCodeData(new QRCodeData("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1"));
        log.info("Check loaded {}", receipt);
        Assert.assertNotNull(receipt);
    }
}
