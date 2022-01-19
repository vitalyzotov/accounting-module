package ru.vzotov.accounting.interfaces.accounting.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.ReceiptItemCategoryPatch;
import ru.vzotov.accounting.test.AbstractControllerTest;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class ReceiptItemsControllerTest extends AbstractControllerTest {

    @Test
    public void patchItemCategory() {
        HttpEntity<ReceiptItemCategoryPatch> request = new HttpEntity<>(new ReceiptItemCategoryPatch("Табак"));
        ResponseEntity<Void> response = this.restTemplate.withBasicAuth(USER, PASSWORD)
                .exchange("/accounting/receipt-items/1?receipt=20180616135500_65624_8710000100313204_110992_2128735201_1", HttpMethod.PATCH, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
