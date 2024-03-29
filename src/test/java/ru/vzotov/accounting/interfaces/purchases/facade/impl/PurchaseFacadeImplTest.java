package ru.vzotov.accounting.interfaces.purchases.facade.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.WithMockPersonUser;
import ru.vzotov.accounting.interfaces.purchases.PurchasesApi;
import ru.vzotov.accounting.interfaces.purchases.facade.PurchasesFacade;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@SpringBootTest
@Transactional
public class PurchaseFacadeImplTest {

    private static final String PERSON_ID = "c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2";

    @Autowired
    private PurchasesFacade facade;

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void findPurchases() {
        List<PurchasesApi.Purchase> purchases = facade.findPurchases(
                LocalDateTime.of(2018, Month.JUNE, 16, 13, 55),
                LocalDateTime.of(2018, Month.JUNE, 16, 13, 56));
        Assertions.assertThat(purchases).
                isNotEmpty().
                hasAtLeastOneElementOfType(PurchasesApi.Purchase.class);
    }

    @Test
    @WithMockPersonUser(person = PERSON_ID)
    public void deletePurchaseById() {
        facade.deletePurchaseById("purchase-80ad2d7a1294");
    }
}
