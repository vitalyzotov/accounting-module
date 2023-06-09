package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi.BudgetCategory;
import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.CategoryNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/accounting/categories")
@CrossOrigin
public class CategoriesController {

    private final AccountingFacade accountingFacade;

    public CategoriesController(AccountingFacade accountingFacade) {
        this.accountingFacade = accountingFacade;
    }

    @GetMapping
    public List<BudgetCategory> listCategories() {
        return accountingFacade.listCategories();
    }

    @GetMapping("{categoryId}")
    public BudgetCategory getCategory(@PathVariable long categoryId) {
        return accountingFacade.getCategory(categoryId);
    }

    @PostMapping
    public BudgetCategory createCategory(@RequestBody BudgetCategory.Create request) {
        return accountingFacade.createCategory(request.name(), request.color(), request.icon());
    }

    @PutMapping("{categoryId}")
    public BudgetCategory modifyCategory(@PathVariable long categoryId, @RequestBody BudgetCategory.Modify request)
            throws CategoryNotFoundException {
        return accountingFacade.modifyCategory(categoryId, request.name(), request.color(), request.icon());
    }

    @DeleteMapping("{categoryId}")
    public BudgetCategory.Ref deleteCategoryById(@PathVariable long categoryId) throws CategoryNotFoundException {
        accountingFacade.deleteCategory(categoryId);
        return new BudgetCategory.Ref(categoryId);
    }

}
