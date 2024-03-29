package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.WithMockPersonUser;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.accounting.facade.ReceiptsFacade;
import ru.vzotov.cashreceipt.application.ReceiptItemNotFoundException;
import ru.vzotov.cashreceipt.application.ReceiptNotFoundException;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ReceiptsFacadeImplTest {

    private static final String PERSON_ID = "c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2";

    @Autowired
    private ReceiptsFacade facade;

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void listAllReceipts() {
        List<AccountingApi.Receipt> receipts = facade.listAllReceipts(
                LocalDate.of(2018, Month.JUNE, 16), LocalDate.of(2018, Month.JUNE, 17));
        Assertions.assertThat(receipts).isNotEmpty();
    }

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void getReceipt() {
        AccountingApi.Receipt receipt = facade.getReceipt("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1");
        assertThat(receipt).isNotNull();
        assertThat(receipt.dateTime()).isEqualTo(LocalDateTime.of(2018, Month.JUNE, 16, 13, 55, 0));

        receipt = facade.getReceipt("t=20200112T1055&s=110.00&fn=9280440300024677&i=35260&fp=1993523059&n=1");
        assertThat(receipt).isNotNull();

        receipt = facade.getReceipt("t=20200112T1056&s=110.00&fn=9280440300024678&i=35261&fp=1993523060&n=1");
        assertThat(receipt).isNotNull();
    }

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void getNotExistingReceipt() {
        AccountingApi.Receipt receipt = facade.getReceipt("t=20180618T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1");
        assertThat(receipt).isNull();
    }

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void listAllCodes() {
        List<AccountingApi.QRCode> codes = facade.listAllCodes(
                LocalDate.of(2018, Month.JUNE, 16), LocalDate.of(2018, Month.JUNE, 17));
        Assertions.assertThat(codes).isNotEmpty();
    }

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void getTimeline() {
        AccountingApi.Timeline timeline = facade.getTimeline();
        assertThat(timeline).isNotNull();
        assertThat(timeline.periods()).isNotEmpty();
    }

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void assignCategoryToItem() throws ReceiptNotFoundException, ReceiptItemNotFoundException {
        facade.assignCategoryToItem(new ReceiptId("20180616135500_65624_8710000100313204_110992_2128735201_1"), 1, "Табак");
        AccountingApi.Receipt receipt = facade.getReceipt("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1");
        assertThat(receipt.items().get(1).category()).isEqualTo("Табак");
    }

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void getAllCategories() {
        List<AccountingApi.PurchaseCategory> categories = facade.getAllCategories();
        Assertions.assertThat(categories).isNotEmpty();
    }

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void getCategory() {
        AccountingApi.PurchaseCategory category = facade.getCategory(new PurchaseCategoryId("id-Табак"));
        assertThat(category.name()).isEqualTo("Табак");
    }

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void createNewCategory() {
        AccountingApi.PurchaseCategory newCategory = facade.createNewCategory("Алкоголь");
        assertThat(newCategory).isNotNull();
        AccountingApi.PurchaseCategory persistentCategory = facade.getCategory(new PurchaseCategoryId(newCategory.categoryId()));
        assertThat(persistentCategory).isNotNull();
        assertThat(persistentCategory.name()).isEqualTo(newCategory.name());
    }

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void renameCategory() throws ReceiptNotFoundException, ReceiptItemNotFoundException {
        AccountingApi.PurchaseCategory category = facade.getCategory(new PurchaseCategoryId("id-12345678901234567890"));
        facade.assignCategoryToItem(new ReceiptId("20180616135500_65624_8710000100313204_110992_2128735201_1"), 0, category.name());
        facade.renameCategory(new PurchaseCategoryId(category.categoryId()), "Пакеты 2");
        AccountingApi.Receipt receipt = facade.getReceipt("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1");
        assertThat(receipt.items().get(0).category()).isEqualTo("Пакеты 2");
    }
}
