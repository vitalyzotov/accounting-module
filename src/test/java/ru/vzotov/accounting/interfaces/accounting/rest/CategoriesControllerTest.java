package ru.vzotov.accounting.interfaces.accounting.rest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetCategoryDTO;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.BudgetCategoryCreateRequest;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.BudgetCategoryModifyRequest;
import ru.vzotov.accounting.test.AbstractControllerTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class CategoriesControllerTest extends AbstractControllerTest {

    private static final String PERSON_ID = "c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2";

    @Test
    public void listCategories() {
        ResponseEntity<List<BudgetCategoryDTO>> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/categories",
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<List<BudgetCategoryDTO>>() {
                }
        );
        assertThat(exchange.getBody())
                .isNotEmpty()
                .usingElementComparatorOnFields("id", "name")
                .contains(new BudgetCategoryDTO(781381038049753674L, PERSON_ID, "Прочие расходы"))
        ;
    }

    @Test
    public void getCategory() {
        ResponseEntity<BudgetCategoryDTO> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/categories/{categoryId}",
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<BudgetCategoryDTO>() {
                }, 781381038049753674L
        );
        assertThat(exchange.getBody())
                .usingRecursiveComparison()
                .isEqualTo(new BudgetCategoryDTO(781381038049753674L, PERSON_ID, "Прочие расходы", "#FF000000", "MyIcon"));
    }

    @Test
    public void createCategory() {
        final String categoryName = "Новая категория";
        ResponseEntity<BudgetCategoryDTO> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/categories",
                HttpMethod.POST, new HttpEntity<>(new BudgetCategoryCreateRequest(categoryName, null, null), null),
                new ParameterizedTypeReference<BudgetCategoryDTO>() {
                }
        );
        assertThat(exchange.getBody())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new BudgetCategoryDTO(0L, PERSON_ID, categoryName));
        assertThat(exchange.getBody().id()).isPositive();
    }

    @Test
    public void renameCategory() {
        final String categoryName = "Переименованная";
        ResponseEntity<BudgetCategoryDTO> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/categories/{categoryId}",
                HttpMethod.PUT, new HttpEntity<>(new BudgetCategoryModifyRequest(categoryName, "#00FF0000", "icon-2"), null),
                new ParameterizedTypeReference<BudgetCategoryDTO>() {
                }, 1000L
        );
        assertThat(exchange.getBody())
                .usingRecursiveComparison()
                .isEqualTo(new BudgetCategoryDTO(1000L, PERSON_ID, categoryName, "#00FF0000", "icon-2"));
    }
}
