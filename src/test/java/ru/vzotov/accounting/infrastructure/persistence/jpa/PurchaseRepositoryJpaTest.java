package ru.vzotov.accounting.infrastructure.persistence.jpa;

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
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.purchase.domain.model.Purchase;
import ru.vzotov.purchase.domain.model.PurchaseId;
import ru.vzotov.purchases.domain.model.PurchaseRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class PurchaseRepositoryJpaTest {

    private static final Logger log = LoggerFactory.getLogger(PurchaseRepositoryJpaTest.class);

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Test
    public void testFind() {
        PurchaseId id = new PurchaseId("20180616135500_2ee_56e0ad53f97dc8ac9ddbc7fa37052b44");

        Purchase p1 = purchaseRepository.find(id);
        assertThat(p1).isNotNull();
        assertThat(p1.checkId()).isEqualTo(new CheckId("20180616135500_65624_8710000100313204_110992_2128735201_1"));
    }

    @Test
    public void testDelete() {
        PurchaseId id = new PurchaseId("20180616135500_834_30920ec538a9d74387dfcd0843ffb4d4");
        assertThat(purchaseRepository.delete(id)).isTrue();
        assertThat(purchaseRepository.find(id)).isNull();
    }
}
