package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.assertj.core.api.Assertions;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.WithMockPersonUser;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.cashreceipt.application.ReceiptItemNotFoundException;
import ru.vzotov.cashreceipt.application.ReceiptNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.ReceiptsFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.ReceiptDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.PurchaseCategoryDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.QRCodeDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.TimelineDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ReceiptsFacadeImplTest {

    private static final String PERSON_ID = "c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2";

    @Autowired
    private ReceiptsFacade facade;

    @Test
    public void listAllReceipts() {
        List<ReceiptDTO> receipts = facade.listAllReceipts(
                LocalDate.of(2018, Month.JUNE, 16), LocalDate.of(2018, Month.JUNE, 17));
        Assertions.assertThat(receipts).isNotEmpty();
    }

    @Test
    public void getReceipt() {
        ReceiptDTO receipt = facade.getReceipt("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1");
        assertThat(receipt).isNotNull();
        assertThat(receipt.getDateTime()).isEqualTo(LocalDateTime.of(2018, Month.JUNE, 16, 13, 55, 0));

        receipt = facade.getReceipt("t=20200112T1055&s=110.00&fn=9280440300024677&i=35260&fp=1993523059&n=1");
        assertThat(receipt).isNotNull();

        receipt = facade.getReceipt("t=20200112T1056&s=110.00&fn=9280440300024678&i=35261&fp=1993523060&n=1");
        assertThat(receipt).isNotNull();
    }

    @Test
    public void getNotExistingReceipt() {
        ReceiptDTO receipt = facade.getReceipt("t=20180618T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1");
        assertThat(receipt).isNull();
    }

    @Test
    public void listAllCodes() {
        List<QRCodeDTO> codes = facade.listAllCodes(
                LocalDate.of(2018, Month.JUNE, 16), LocalDate.of(2018, Month.JUNE, 17));
        Assertions.assertThat(codes).isNotEmpty();
    }

    @Test
    public void getTimeline() {
        TimelineDTO timeline = facade.getTimeline();
        assertThat(timeline).isNotNull();
        assertThat(timeline.getPeriods()).isNotEmpty();
    }

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void assignCategoryToItem() throws ReceiptNotFoundException, ReceiptItemNotFoundException {
        facade.assignCategoryToItem(new ReceiptId("20180616135500_65624_8710000100313204_110992_2128735201_1"), 1, "Табак");
        ReceiptDTO receipt = facade.getReceipt("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1");
        assertThat(receipt.getItems().get(1).getCategory()).isEqualTo("Табак");
    }

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void getAllCategories() {
        List<PurchaseCategoryDTO> categories = facade.getAllCategories();
        Assertions.assertThat(categories).isNotEmpty();
    }

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void getCategory() {
        PurchaseCategoryDTO category = facade.getCategory(new PurchaseCategoryId("id-Табак"));
        assertThat(category.getName()).isEqualTo("Табак");
    }

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void createNewCategory() {
        PurchaseCategoryDTO newCategory = facade.createNewCategory("Алкоголь");
        assertThat(newCategory).isNotNull();
        PurchaseCategoryDTO persistentCategory = facade.getCategory(new PurchaseCategoryId(newCategory.getCategoryId()));
        assertThat(persistentCategory).isNotNull();
        assertThat(persistentCategory.getName()).isEqualTo(newCategory.getName());
    }

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void renameCategory() throws ReceiptNotFoundException, ReceiptItemNotFoundException {
        PurchaseCategoryDTO category = facade.getCategory(new PurchaseCategoryId("id-12345678901234567890"));
        facade.assignCategoryToItem(new ReceiptId("20180616135500_65624_8710000100313204_110992_2128735201_1"), 0, category.getName());
        facade.renameCategory(new PurchaseCategoryId(category.getCategoryId()), "Пакеты 2");
        ReceiptDTO receipt = facade.getReceipt("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1");
        assertThat(receipt.getItems().get(0).getCategory()).isEqualTo("Пакеты 2");
    }
}
