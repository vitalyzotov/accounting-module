package ru.vzotov.accounting.interfaces.accounting.rest;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.test.AbstractControllerTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class PurchaseCategoriesControllerTest extends AbstractControllerTest {

    private static final String PERSON_ID = "c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2";

    @Test
    public void listCategories() {
        ResponseEntity<List<AccountingApi.PurchaseCategory>> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/purchase-categories",
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<>() {
                }
        );
        Assertions.assertThat(exchange.getBody())
                .isNotEmpty()
                .have(new Condition<>() {
                    @Override
                    public boolean matches(AccountingApi.PurchaseCategory value) {
                        return value.categoryId() != null && !value.categoryId().isEmpty();
                    }
                })
                .haveExactly(1, new Condition<>() {
                    @Override
                    public boolean matches(AccountingApi.PurchaseCategory value) {
                        return value.name().equals("Табак");
                    }
                })
        ;
    }

    @Test
    public void getCategory() {
        final String catId = "id-Табак";
        ResponseEntity<AccountingApi.PurchaseCategory> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/purchase-categories/" + catId,
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(exchange.getBody())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(new AccountingApi.PurchaseCategory(catId, PERSON_ID, "Табак"));
    }

    @Test
    public void createNewCategory() {
        final String newCategoryName = "Новая категория";
        ResponseEntity<AccountingApi.PurchaseCategory> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/purchase-categories",
                HttpMethod.POST, new HttpEntity<>(new AccountingApi.PurchaseCategory(null, PERSON_ID, newCategoryName)),
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(exchange.getBody())
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("categoryId")
                .isEqualTo(new AccountingApi.PurchaseCategory(null, PERSON_ID, newCategoryName));
        assertThat(exchange.getBody().categoryId())
                .isNotEmpty();
    }

    @Test
    public void renameCategory() {
        final String newCategoryName = "Новая категория";
        final String newCategoryName2 = "категория переименованная";
        AccountingApi.PurchaseCategory mycat = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/purchase-categories",
                HttpMethod.POST, new HttpEntity<>(new AccountingApi.PurchaseCategory(null, PERSON_ID, newCategoryName)),
                new ParameterizedTypeReference<AccountingApi.PurchaseCategory>() {
                }
        ).getBody();

        assertThat(mycat).isNotNull();

        ResponseEntity<AccountingApi.PurchaseCategory> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/purchase-categories/" + mycat.categoryId(),
                HttpMethod.PATCH, new HttpEntity<>(new AccountingApi.PurchaseCategory(null, PERSON_ID, newCategoryName2)),
                new ParameterizedTypeReference<>() {
                }
        );

        assertThat(exchange.getBody())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(new AccountingApi.PurchaseCategory(mycat.categoryId(), PERSON_ID, newCategoryName2));
    }
}
