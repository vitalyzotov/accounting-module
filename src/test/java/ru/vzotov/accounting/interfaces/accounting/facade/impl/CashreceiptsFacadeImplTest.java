package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.assertj.core.api.Assertions;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.cashreceipt.application.ReceiptItemNotFoundException;
import ru.vzotov.cashreceipt.application.ReceiptNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.facade.CashreceiptsFacade;
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
public class CashreceiptsFacadeImplTest {

    @Autowired
    private CashreceiptsFacade facade;

    @Test
    public void listAllChecks() {
        List<ReceiptDTO> checks = facade.listAllChecks(
                LocalDate.of(2018, Month.JUNE, 16), LocalDate.of(2018, Month.JUNE, 17));
        Assertions.assertThat(checks).isNotEmpty();
    }

    @Test
    public void getCheck() {
        ReceiptDTO check = facade.getCheck("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1");
        assertThat(check).isNotNull();
        assertThat(check.getDateTime()).isEqualTo(LocalDateTime.of(2018, Month.JUNE, 16, 13, 55, 0));

        check = facade.getCheck("t=20200112T1055&s=110.00&fn=9280440300024677&i=35260&fp=1993523059&n=1");
        assertThat(check).isNotNull();

        check = facade.getCheck("t=20200112T1056&s=110.00&fn=9280440300024678&i=35261&fp=1993523060&n=1");
        assertThat(check).isNotNull();
    }

    @Test
    public void getNotExistingCheck() {
        ReceiptDTO check = facade.getCheck("t=20180618T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1");
        assertThat(check).isNull();
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
    public void assignCategoryToItem() throws ReceiptNotFoundException, ReceiptItemNotFoundException {
        facade.assignCategoryToItem(new CheckId("20180616135500_65624_8710000100313204_110992_2128735201_1"), 1, "Табак");
        ReceiptDTO check = facade.getCheck("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1");
        assertThat(check.getItems().get(1).getCategory()).isEqualTo("Табак");
    }

    @Test
    public void getAllCategories() {
        List<PurchaseCategoryDTO> categories = facade.getAllCategories();
        Assertions.assertThat(categories).isNotEmpty();
    }

    @Test
    public void getCategory() {
        PurchaseCategoryDTO category = facade.getCategory(new PurchaseCategoryId("id-Табак"));
        assertThat(category.getName()).isEqualTo("Табак");
    }

    @Test
    public void createNewCategory() {
        PurchaseCategoryDTO newCategory = facade.createNewCategory("Алкоголь");
        assertThat(newCategory).isNotNull();
        PurchaseCategoryDTO persistentCategory = facade.getCategory(new PurchaseCategoryId(newCategory.getCategoryId()));
        assertThat(persistentCategory).isNotNull();
        assertThat(persistentCategory.getName()).isEqualTo(newCategory.getName());
    }

    @Test
    public void renameCategory() throws ReceiptNotFoundException, ReceiptItemNotFoundException {
        PurchaseCategoryDTO category = facade.getCategory(new PurchaseCategoryId("id-12345678901234567890"));
        facade.assignCategoryToItem(new CheckId("20180616135500_65624_8710000100313204_110992_2128735201_1"), 0, category.getName());
        facade.renameCategory(new PurchaseCategoryId(category.getCategoryId()), "Пакеты 2");
        ReceiptDTO check = facade.getCheck("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1");
        assertThat(check.getItems().get(0).getCategory()).isEqualTo("Пакеты 2");
    }
}