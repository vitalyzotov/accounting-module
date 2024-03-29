package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.Budget;
import ru.vzotov.accounting.domain.model.BudgetDirection;
import ru.vzotov.accounting.domain.model.BudgetId;
import ru.vzotov.accounting.domain.model.BudgetPlan;
import ru.vzotov.accounting.domain.model.BudgetPlanId;
import ru.vzotov.accounting.domain.model.BudgetPlanRepository;
import ru.vzotov.accounting.domain.model.BudgetRepository;
import ru.vzotov.accounting.domain.model.BudgetRule;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.domain.model.Money;

import jakarta.transaction.Transactional;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class BudgetPlanRepositoryJpaTest {
    private static final Logger log = LoggerFactory.getLogger(BudgetPlanRepositoryJpaTest.class);

    @Autowired
    private BudgetPlanRepository budgetPlanRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Test
    public void testStore() {
        Budget budget = budgetRepository.find(new BudgetId("test-budget-2"));
        BudgetRule rule = budget.rules().stream().filter(r -> r.ruleId().value().equals("013")).findAny().orElse(null);
        BudgetPlan item = new BudgetPlan(
                BudgetPlanId.nextId(),
                rule,
                LocalDate.of(2020, 1, 15),
                BudgetDirection.EXPENSE,
                Money.rubles(15000),
                new AccountNumber("40817810108290123456"),
                null,
                null, null
        );
        budgetPlanRepository.store(item);
    }

    @Test
    public void testFind() {
        BudgetPlan item = budgetPlanRepository.find(new BudgetPlanId("BBI-000-000-011"));
        assertThat(item).isNotNull();
        assertThat(item.rule()).hasFieldOrPropertyWithValue("name", "Гипермаркет");
    }

    @Test
    public void testFindAll() {
        assertThat(budgetPlanRepository.findAll()).isNotEmpty();
    }

    @Test
    public void testDelete() {
        final BudgetPlanId itemId = new BudgetPlanId("BBI-DEL-000-011");
        assertThat(budgetPlanRepository.delete(itemId)).isTrue();
        assertThat(budgetPlanRepository.find(itemId)).isNull();
    }

}
