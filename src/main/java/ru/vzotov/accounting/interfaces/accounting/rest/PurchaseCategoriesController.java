package ru.vzotov.accounting.interfaces.accounting.rest;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;
import ru.vzotov.accounting.interfaces.accounting.facade.ReceiptsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounting/purchase-categories")
@CrossOrigin
public class PurchaseCategoriesController {

    private final ReceiptsFacade receiptsFacade;

    @Autowired
    public PurchaseCategoriesController(ReceiptsFacade receiptsFacade) {
        this.receiptsFacade = receiptsFacade;
    }

    @GetMapping
    public List<AccountingApi.PurchaseCategory> listCategories() {
        return receiptsFacade.getAllCategories();
    }

    @GetMapping("/{categoryId}")
    public AccountingApi.PurchaseCategory getCategory(@PathVariable String categoryId) {
        return receiptsFacade.getCategory(new PurchaseCategoryId(categoryId));
    }

    @PostMapping
    public AccountingApi.PurchaseCategory createNewCategory(@RequestBody AccountingApi.PurchaseCategory category) {
        return receiptsFacade.createNewCategory(category.name());
    }

    @PatchMapping("/{categoryId}")
    public AccountingApi.PurchaseCategory renameCategory(@PathVariable String categoryId, @RequestBody AccountingApi.PurchaseCategory category) {
        return receiptsFacade.renameCategory(new PurchaseCategoryId(categoryId), category.name());
    }
}
