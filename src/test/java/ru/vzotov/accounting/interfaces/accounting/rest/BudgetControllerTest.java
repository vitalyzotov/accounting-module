package ru.vzotov.accounting.interfaces.accounting.rest;

import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetRuleDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.MoneyDTO;
import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.accounting.domain.model.BudgetRuleId;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class BudgetControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        // PATCH support
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    public void getRule() {
        final String budgetId = "test-budget-1";
        final ResponseEntity<BudgetRuleDTO> exchange = this.restTemplate.exchange(
                "/accounting/budget/{budgetId}/rule/{ruleId}",
                HttpMethod.GET, new HttpEntity<>(null),
                BudgetRuleDTO.class,
                budgetId,
                new BudgetRuleId("004").value()
        );

        assertThat(exchange.getBody().getCategoryId()).isEqualTo(781381038049753674L);
    }

    @Test
    public void replaceRule() {
        final String budgetId = "test-budget-1";
        final ResponseEntity<BudgetDTO> exchange = this.restTemplate.exchange(
                "/accounting/budget/{budgetId}/rule/{ruleId}",
                HttpMethod.PUT, new HttpEntity<>(new BudgetRuleDTO(
                        "001",
                        "+", "Заработная плата 2",
                        null,
                        null,
                        "40817810108290123456",
                        "M2020-01-01(1)[8]",
                        new MoneyDTO(2900000, "RUR"),
                        null,
                        null
                )),
                BudgetDTO.class,
                budgetId,
                new BudgetRuleId("001").value()
        );

        assertThat(exchange.getBody().getRules()).isNotEmpty();

        assertThat(this.restTemplate.exchange(
                "/accounting/budget/{budgetId}",
                HttpMethod.GET, new HttpEntity<>(null),
                BudgetDTO.class, budgetId
        ).getBody().getRules()).doNotHave(new Condition<BudgetRuleDTO>() {
            @Override
            public boolean matches(BudgetRuleDTO rule) {
                return rule.getName().equals("Заработная плата");
            }
        }).haveExactly(1, new Condition<BudgetRuleDTO>() {
            @Override
            public boolean matches(BudgetRuleDTO rule) {
                return rule.getName().equals("Заработная плата 2");
            }
        });
    }

    @Test
    public void deleteRule() {
        final String budgetId = "test-budget-1";
        final ResponseEntity<BudgetDTO> exchange = this.restTemplate.exchange(
                "/accounting/budget/{budgetId}/rule/{ruleId}",
                HttpMethod.DELETE, new HttpEntity<>(null),
                BudgetDTO.class,
                budgetId,
                new BudgetRuleId("003").value() //.ruleIdOf("Гипермаркет")
        );

        assertThat(exchange.getBody().getRules()).isNotEmpty();

        assertThat(this.restTemplate.exchange(
                "/accounting/budget/{budgetId}",
                HttpMethod.GET, new HttpEntity<>(null),
                BudgetDTO.class, budgetId
        ).getBody().getRules()).doNotHave(new Condition<BudgetRuleDTO>() {
            @Override
            public boolean matches(BudgetRuleDTO rule) {
                return rule.getName().equals("Гипермаркет");
            }
        }).haveExactly(1, new Condition<BudgetRuleDTO>() {
            @Override
            public boolean matches(BudgetRuleDTO rule) {
                return rule.getName().equals("Аванс");
            }
        });
    }
}
