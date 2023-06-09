package ru.vzotov.accounting.interfaces.accounting.rest;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import ru.vzotov.accounting.domain.model.BudgetRuleId;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.test.AbstractControllerTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class BudgetControllerTest extends AbstractControllerTest {

    @Test
    public void getRule() {
        final String budgetId = "test-budget-1";
        final ResponseEntity<AccountingApi.BudgetRule> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/budget/{budgetId}/rule/{ruleId}",
                HttpMethod.GET, new HttpEntity<>(null),
                AccountingApi.BudgetRule.class,
                budgetId,
                new BudgetRuleId("004").value()
        );

        assertThat(exchange.getBody().categoryId()).isEqualTo(781381038049753674L);
    }

    @Test
    public void replaceRule() {
        final String budgetId = "test-budget-1";
        final ResponseEntity<AccountingApi.Budget> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/budget/{budgetId}/rule/{ruleId}",
                HttpMethod.PUT, new HttpEntity<>(new AccountingApi.BudgetRule(
                        "001",
                        "+", "Заработная плата 2",
                        null,
                        null,
                        null,
                        "40817810108290123456",
                        "M2020-01-01(1)[8]",
                        new AccountingApi.Money(2900000, "RUR"),
                        null,
                        null
                )),
                AccountingApi.Budget.class,
                budgetId,
                new BudgetRuleId("001").value()
        );

        assertThat(exchange.getBody().rules()).isNotEmpty();

        assertThat(this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/budget/{budgetId}",
                HttpMethod.GET, new HttpEntity<>(null),
                AccountingApi.Budget.class, budgetId
        ).getBody().rules()).doNotHave(new Condition<AccountingApi.BudgetRule>() {
            @Override
            public boolean matches(AccountingApi.BudgetRule rule) {
                return rule.name().equals("Заработная плата");
            }
        }).haveExactly(1, new Condition<AccountingApi.BudgetRule>() {
            @Override
            public boolean matches(AccountingApi.BudgetRule rule) {
                return rule.name().equals("Заработная плата 2");
            }
        });
    }

    @Test
    public void deleteRule() {
        final String budgetId = "test-budget-1";
        final ResponseEntity<AccountingApi.Budget> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/budget/{budgetId}/rule/{ruleId}",
                HttpMethod.DELETE, new HttpEntity<>(null),
                AccountingApi.Budget.class,
                budgetId,
                new BudgetRuleId("003").value() //.ruleIdOf("Гипермаркет")
        );

        assertThat(exchange.getBody().rules()).isNotEmpty();

        assertThat(this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/budget/{budgetId}",
                HttpMethod.GET, new HttpEntity<>(null),
                AccountingApi.Budget.class, budgetId
        ).getBody().rules()).doNotHave(new Condition<AccountingApi.BudgetRule>() {
            @Override
            public boolean matches(AccountingApi.BudgetRule rule) {
                return rule.name().equals("Гипермаркет");
            }
        }).haveExactly(1, new Condition<AccountingApi.BudgetRule>() {
            @Override
            public boolean matches(AccountingApi.BudgetRule rule) {
                return rule.name().equals("Аванс");
            }
        });
    }
}
