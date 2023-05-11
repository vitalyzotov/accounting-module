package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.Deal;
import ru.vzotov.accounting.domain.model.DealId;
import ru.vzotov.accounting.domain.model.DealRepository;
import ru.vzotov.banking.domain.model.BudgetCategoryId;
import ru.vzotov.banking.domain.model.OperationId;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.domain.model.Money;
import ru.vzotov.person.domain.model.PersonId;
import ru.vzotov.purchase.domain.model.PurchaseId;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class DealRepositoryJpaTest {
    private static final Logger log = LoggerFactory.getLogger(DealRepositoryJpaTest.class);
    private static final String RECEIPT_ID_1 = "test16_receipt_1";
    private static final PersonId PERSON_ID = new PersonId("c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2");

    @Autowired
    private DealRepository dealRepository;

    @Test
    public void find() {
        Deal deal = dealRepository.find(new DealId("deal-1"));
        log.info("Deal loaded {}", deal);
        assertThat(deal)
                .isNotNull()
        ;
        assertThat(deal.operations()).contains(new OperationId("test-operation-1"));
        assertThat(deal.receipts()).contains(new ReceiptId(RECEIPT_ID_1));
    }

    @Test
    public void findByOperation() {
        final OperationId operation = new OperationId("test-operation-1");
        Deal deal = dealRepository.findByOperation(operation);
        assertThat(deal).isNotNull();
        assertThat(deal.operations()).contains(operation);
    }

    @Test
    public void findNullByOperation() {
        final OperationId operation = new OperationId("non-existing-operation-1");
        Deal deal = dealRepository.findByOperation(operation);
        assertThat(deal).isNull();
    }

    @Test
    public void findByReceipt() {
        final ReceiptId receipt = new ReceiptId("test16_receipt_1");
        Deal deal = dealRepository.findByReceipt(receipt);
        assertThat(deal).isNotNull();
        assertThat(deal.receipts()).contains(receipt);
    }

    @Test
    public void findEmptyResult() {
        final List<Deal> emptyList = dealRepository.findByDate(
                LocalDate.of(2017, 1, 1),
                LocalDate.of(2017, 7, 9));
        assertThat(emptyList)
                .as("empty list expected")
                .isEmpty();
    }

    @Test
    public void findBetweenDates() {
        final List<Deal> result = dealRepository.findByDate(
                LocalDate.of(2017, 7, 1),
                LocalDate.of(2017, 7, 10));
        assertThat(result)
                .as("at least one deal expected")
                .isNotEmpty();
    }

    @Test
    public void findByQuery() {
        final List<Deal> result = dealRepository.findByDate(
                singleton(PERSON_ID),
                "AcCio",
                LocalDate.of(2017, 7, 1),
                LocalDate.of(2022, 1, 1));
        assertThat(result)
                .as("at least one deal expected")
                .isNotEmpty()
                .extracting(Deal::dealId)
                .containsExactly(new DealId("deal-1"))
        ;
    }

    @Test
    @Rollback(false) // we need flush for element collections
    public void store() {
        Deal deal = new Deal(
                DealId.nextId(),
                PERSON_ID,
                LocalDate.of(2021, 11, 21),
                Money.kopecks(123456),
                "description", "comment",
                new BudgetCategoryId(781381038049753674L),
                singleton(new ReceiptId("receipt-d06eeb5c598c")),
                singleton(new OperationId("deal-operation-4")),
                Collections.emptySet(),
                Collections.singletonList(new PurchaseId("purchase-0912fc12d100"))
        );
        dealRepository.store(deal);
    }

    @Test
    public void delete() {
        dealRepository.delete(dealRepository.find(new DealId("deal-for-remove-2")));
    }

    @Test
    public void findMinMaxDates() {
        LocalDate min = dealRepository.findMinDealDate(singleton(PERSON_ID));
        LocalDate max = dealRepository.findMaxDealDate(singleton(PERSON_ID));
        assertThat(min).isNotNull().isEqualTo(LocalDate.of(2017, 7, 10));
        assertThat(max).isNotNull().isAfter(min);

        LocalDate[] minmax = dealRepository.findMinMaxDealDates(singleton(PERSON_ID));
        assertThat(minmax).isNotNull().hasSize(2);
        assertThat(minmax[0]).isNotNull().isEqualTo(LocalDate.of(2017, 7, 10));
        assertThat(minmax[1]).isNotNull().isAfter(min);

    }
}
