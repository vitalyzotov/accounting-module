package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.PurchaseCategory;
import ru.vzotov.accounting.interfaces.accounting.facade.ReceiptsFacade;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryId;

import java.util.List;

@RestController
@RequestMapping("/accounting/purchase-categories")
@CrossOrigin
public class PurchaseCategoriesController {

    private final ReceiptsFacade receiptsFacade;

    public PurchaseCategoriesController(ReceiptsFacade receiptsFacade) {
        this.receiptsFacade = receiptsFacade;
    }

    @GetMapping
    public List<PurchaseCategory> listCategories() {
        return receiptsFacade.getAllCategories();
    }

    @GetMapping("/{categoryId}")
    public PurchaseCategory getCategory(@PathVariable String categoryId) {
        return receiptsFacade.getCategory(new PurchaseCategoryId(categoryId));
    }

    @PostMapping
    public PurchaseCategory createNewCategory(@RequestBody PurchaseCategory.Create category) {
        return receiptsFacade.createNewCategory(category.name());
    }

    @PatchMapping("/{categoryId}")
    public PurchaseCategory renameCategory(@PathVariable String categoryId,
                                           @RequestBody PurchaseCategory.Modify category) {
        return receiptsFacade.renameCategory(new PurchaseCategoryId(categoryId), category.name());
    }
}
