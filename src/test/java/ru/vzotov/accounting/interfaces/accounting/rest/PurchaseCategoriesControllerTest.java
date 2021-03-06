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

    private static final String PERSON_ID = "c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2";

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
                        return value.getName().equals("??????????");
                    }
                })
        ;
    }

    @Test
    public void getCategory() {
        final String catId = "id-??????????";
        ResponseEntity<PurchaseCategoryDTO> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/purchase-categories/" + catId,
                HttpMethod.GET, new HttpEntity<>(null),
                new ParameterizedTypeReference<PurchaseCategoryDTO>() {
                }
        );
        assertThat(exchange.getBody())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(new PurchaseCategoryDTO(catId, PERSON_ID, "??????????"));
    }

    @Test
    public void createNewCategory() {
        final String newCategoryName = "?????????? ??????????????????";
        ResponseEntity<PurchaseCategoryDTO> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/purchase-categories",
                HttpMethod.POST, new HttpEntity<>(new PurchaseCategoryDTO(null, PERSON_ID, newCategoryName)),
                new ParameterizedTypeReference<PurchaseCategoryDTO>() {
                }
        );
        assertThat(exchange.getBody())
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("categoryId")
                .isEqualTo(new PurchaseCategoryDTO(null, PERSON_ID, newCategoryName));
        assertThat(exchange.getBody().getCategoryId())
                .isNotEmpty();
    }

    @Test
    public void renameCategory() {
        final String newCategoryName = "?????????? ??????????????????";
        final String newCategoryName2 = "?????????????????? ??????????????????????????????";
        PurchaseCategoryDTO mycat = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/purchase-categories",
                HttpMethod.POST, new HttpEntity<>(new PurchaseCategoryDTO(null, PERSON_ID, newCategoryName)),
                new ParameterizedTypeReference<PurchaseCategoryDTO>() {
                }
        ).getBody();

        ResponseEntity<PurchaseCategoryDTO> exchange = this.restTemplate.withBasicAuth(USER, PASSWORD).exchange(
                "/accounting/purchase-categories/" + mycat.getCategoryId(),
                HttpMethod.PATCH, new HttpEntity<>(new PurchaseCategoryDTO(null, PERSON_ID, newCategoryName2)),
                new ParameterizedTypeReference<PurchaseCategoryDTO>() {
                }
        );

        assertThat(exchange.getBody())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(new PurchaseCategoryDTO(mycat.getCategoryId(), PERSON_ID, newCategoryName2));
    }
}
