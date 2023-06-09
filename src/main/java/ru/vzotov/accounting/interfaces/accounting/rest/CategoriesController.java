package ru.vzotov.accounting.interfaces.accounting.rest;

import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.CategoryNotFoundException;
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

import java.util.List;

@RestController
@RequestMapping("/accounting/categories")
@CrossOrigin
public class CategoriesController {

    @Autowired
    private AccountingFacade accountingFacade;

    @GetMapping
    public List<AccountingApi.BudgetCategory> listCategories() {
        return accountingFacade.listCategories();
    }

    @GetMapping("{categoryId}")
    public AccountingApi.BudgetCategory getCategory(@PathVariable long categoryId) {
        return accountingFacade.getCategory(categoryId);
    }

    @PostMapping
    public AccountingApi.BudgetCategory createCategory(@RequestBody AccountingApi.BudgetCategoryCreateRequest request) {
        return accountingFacade.createCategory(request.name(), request.color(), request.icon());
    }

    @PutMapping("{categoryId}")
    public AccountingApi.BudgetCategory modifyCategory(@PathVariable long categoryId, @RequestBody AccountingApi.BudgetCategoryModifyRequest request) throws CategoryNotFoundException {
        return accountingFacade.modifyCategory(categoryId, request.name(), request.color(), request.icon());
    }

    @DeleteMapping("{categoryId}")
    public AccountingApi.BudgetCategoryDeleteResponse deleteCategoryById(@PathVariable long categoryId) throws CategoryNotFoundException {
        accountingFacade.deleteCategory(categoryId);
        return new AccountingApi.BudgetCategoryDeleteResponse(categoryId);
    }

}
