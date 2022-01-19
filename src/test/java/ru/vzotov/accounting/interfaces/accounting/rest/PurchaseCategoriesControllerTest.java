package ru.vzotov.accounting.interfaces.accounting.rest;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.PurchaseCategoryDTO;
import ru.vzotov.accounting.test.AbstractControllerTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class PurchaseCategoriesControllerTest extends AbstractControllerTest {

    @Test
    public void listCategories() {
        ResponseEntity<List<PurchaseCategoryDTO>> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/purchase-categories",
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<List<PurchaseCategoryDTO>>() {
                }
        );
        Assertions.assertThat(exchange.getBody())
                .isNotEmpty()
                .have(new Condition<PurchaseCategoryDTO>() {
                    @Override
                    public boolean matches(PurchaseCategoryDTO value) {
                        return value.getCategoryId() != null && !value.getCategoryId().isEmpty();
                    }
                })
                .haveExactly(1, new Condition<PurchaseCategoryDTO>() {
                    @Override
                    public boolean matches(PurchaseCategoryDTO value) {
                        return value.getName().equals("Табак");
                    }
                })
        ;
    }

    @Test
    public void getCategory() {
        final String catId = "id-Табак";
        ResponseEntity<PurchaseCategoryDTO> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/purchase-categories/" + catId,
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<PurchaseCategoryDTO>() {
                }
        );
        assertThat(exchange.getBody())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(new PurchaseCategoryDTO(catId, "Табак"));
    }

    @Test
    public void createNewCategory() {
        final String newCategoryName = "Новая категория";
        ResponseEntity<PurchaseCategoryDTO> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/purchase-categories",
                HttpMethod.POST, new HttpEntity<>(new PurchaseCategoryDTO(null, newCategoryName)),
                new ParameterizedTypeReference<PurchaseCategoryDTO>() {
                }
        );
        assertThat(exchange.getBody())
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("categoryId")
                .isEqualTo(new PurchaseCategoryDTO(null, newCategoryName));
        assertThat(exchange.getBody().getCategoryId())
                .isNotEmpty();
    }

    @Test
    public void renameCategory() {
        final String newCategoryName = "Новая категория";
        final String newCategoryName2 = "категория переименованная";
        PurchaseCategoryDTO mycat = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/purchase-categories",
                HttpMethod.POST, new HttpEntity<>(new PurchaseCategoryDTO(null, newCategoryName)),
                new ParameterizedTypeReference<PurchaseCategoryDTO>() {
                }
        ).getBody();

        ResponseEntity<PurchaseCategoryDTO> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/purchase-categories/" + mycat.getCategoryId(),
                HttpMethod.PATCH, new HttpEntity<>(new PurchaseCategoryDTO(null, newCategoryName2)),
                new ParameterizedTypeReference<PurchaseCategoryDTO>() {
                }
        );

        assertThat(exchange.getBody())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(new PurchaseCategoryDTO(mycat.getCategoryId(), newCategoryName2));
    }
}
