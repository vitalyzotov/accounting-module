package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.BudgetRepository;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.accounting.domain.model.Budget;
import ru.vzotov.accounting.domain.model.BudgetId;
import ru.vzotov.accounting.domain.model.BudgetRule;
import ru.vzotov.accounting.domain.model.BudgetRuleId;
import ru.vzotov.accounting.domain.model.BudgetRuleType;
import ru.vzotov.accounting.domain.model.Calculation;
import ru.vzotov.banking.domain.model.AccountNumber;
import ru.vzotov.calendar.domain.model.Recurrence;
import ru.vzotov.calendar.domain.model.RecurrenceUnit;
import ru.vzotov.domain.model.Money;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class BudgetRepositoryJpaTest {
    private static final Logger log = LoggerFactory.getLogger(BudgetRepositoryJpaTest.class);

    @Autowired
    private BudgetRepository budgetRepository;

    @Test
    public void testStore() {
        LocalDate start = LocalDate.of(2020, Month.JANUARY, 1);
        LocalDate finish = LocalDate.of(2020, Month.MARCH, 31);
        Set<BudgetRule> rules = new HashSet<>();
        AccountNumber workAccount = new AccountNumber("40817810108290123456");

        rules.add(new BudgetRule(
                BudgetRuleId.nextId(),
                BudgetRuleType.INCOME,
                null,
                null,
                workAccount,
                new Recurrence(start, null, RecurrenceUnit.MONTHLY, 1, 7),
                "Заработная плата",
                Money.rubles(28000.0d),
                new Calculation("value.amount().doubleValue() * (calendar.workdaysBetween(prevMonth.atDay(16), prevMonth.atEndOfMonth()) / calendar.workdaysBetween(prevMonth.atDay(1), prevMonth.atEndOfMonth()));")
        ));

        rules.add(new BudgetRule(
                BudgetRuleId.nextId(),
                BudgetRuleType.INCOME,
                null,
                null,
                workAccount,
                new Recurrence(start, null, RecurrenceUnit.MONTHLY, 1, 22),
                "Аванс",
                Money.rubles(28000.0d),
                new Calculation("value.amount().doubleValue() * (calendar.workdaysBetween(month.atDay(1), month.atDay(16)) / calendar.workdaysBetween(month.atDay(1), month.atEndOfMonth()));")
        ));

        rules.add(new BudgetRule(
                BudgetRuleId.nextId(),
                BudgetRuleType.EXPENSE,
                null,
                workAccount,
                null,
                new Recurrence(start, finish, RecurrenceUnit.WEEKLY),
                "Гипермаркет",
                Money.rubles(1500.0d)
        ));

        Budget budget = new Budget(BudgetId.nextId(), "default", rules);
        budgetRepository.store(budget);
    }

    @Test
    public void testFind() {
        Budget budget = budgetRepository.find(new BudgetId("test-budget-1"));
        assertThat(budget).isNotNull();
        assertThat(budget.rules())
                .hasSize(4)
                .are(new Condition<BudgetRule>() {
                    @Override
                    public boolean matches(BudgetRule value) {
                        return value.sourceAccount() != null || value.targetAccount() != null;
                    }
                })
        ;
    }

    @Test
    public void testFindAll() {
        assertThat(budgetRepository.findAll()).isNotEmpty();
    }

    @Test
    public void testDelete() {
        final BudgetId budgetId = new BudgetId("removed-budget-2");
        assertThat(budgetRepository.delete(budgetId)).isTrue();
        assertThat(budgetRepository.find(budgetId)).isNull();
    }
}
