package ru.vzotov.accounting.interfaces.purchases.facade.impl;

import org.assertj.core.api.Assertions;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.interfaces.purchases.facade.PurchasesFacade;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PurchaseFacadeImplTest {

    @Autowired
    private PurchasesFacade facade;


    @Test
    public void findPurchases() {
        List<PurchaseDTO> purchases = facade.findPurchases(
                LocalDateTime.of(2018, Month.JUNE, 16, 13, 55),
                LocalDateTime.of(2018, Month.JUNE, 16, 13, 56));
        Assertions.assertThat(purchases).
                isNotEmpty().
                hasAtLeastOneElementOfType(PurchaseDTO.class);
    }

    @Test
    public void deletePurchaseById() {
        facade.deletePurchaseById("purchase-80ad2d7a1294");
    }
}
