package ru.vzotov.accounting.interfaces.accounting.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.test.AbstractControllerTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class CategoriesControllerTest extends AbstractControllerTest {

    private static final String PERSON_ID = "c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2";

    @Test
    public void listCategories() {
        ResponseEntity<List<AccountingApi.BudgetCategory>> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/categories",
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<List<AccountingApi.BudgetCategory>>() {
                }
        );
        assertThat(exchange.getBody())
                .isNotEmpty()
                .usingElementComparatorOnFields("id", "name")
                .contains(new AccountingApi.BudgetCategory(781381038049753674L, PERSON_ID, "Прочие расходы"))
        ;
    }

    @Test
    public void getCategory() {
        ResponseEntity<AccountingApi.BudgetCategory> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/categories/{categoryId}",
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<AccountingApi.BudgetCategory>() {
                }, 781381038049753674L
        );
        assertThat(exchange.getBody())
                .usingRecursiveComparison()
                .isEqualTo(new AccountingApi.BudgetCategory(781381038049753674L, PERSON_ID, "Прочие расходы", "#FF000000", "MyIcon"));
    }

    @Test
    public void createCategory() {
        final String categoryName = "Новая категория";
        ResponseEntity<AccountingApi.BudgetCategory> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/categories",
                HttpMethod.POST, new HttpEntity<>(new AccountingApi.BudgetCategoryCreateRequest(categoryName, null, null), null),
                new ParameterizedTypeReference<AccountingApi.BudgetCategory>() {
                }
        );
        assertThat(exchange.getBody())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new AccountingApi.BudgetCategory(0L, PERSON_ID, categoryName));
        assertThat(exchange.getBody().id()).isPositive();
    }

    @Test
    public void renameCategory() {
        final String categoryName = "Переименованная";
        ResponseEntity<AccountingApi.BudgetCategory> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/categories/{categoryId}",
                HttpMethod.PUT, new HttpEntity<>(new AccountingApi.BudgetCategoryModifyRequest(categoryName, "#00FF0000", "icon-2"), null),
                new ParameterizedTypeReference<AccountingApi.BudgetCategory>() {
                }, 1000L
        );
        assertThat(exchange.getBody())
                .usingRecursiveComparison()
                .isEqualTo(new AccountingApi.BudgetCategory(1000L, PERSON_ID, categoryName, "#00FF0000", "icon-2"));
    }
}
