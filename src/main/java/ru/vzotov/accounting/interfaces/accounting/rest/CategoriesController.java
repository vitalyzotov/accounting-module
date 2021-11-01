package ru.vzotov.accounting.interfaces.accounting.rest;

import ru.vzotov.accounting.interfaces.accounting.facade.AccountingFacade;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.BudgetCategoryDTO;
import ru.vzotov.accounting.interfaces.accounting.facade.dto.CategoryNotFoundException;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.BudgetCategoryCreateRequest;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.BudgetCategoryDeleteResponse;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.BudgetCategoryModifyRequest;
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
    public List<BudgetCategoryDTO> listCategories() {
        return accountingFacade.listCategories();
    }

    @GetMapping("{categoryId}")
    public BudgetCategoryDTO getCategory(@PathVariable long categoryId) {
        return accountingFacade.getCategory(categoryId);
    }

    @PostMapping
    public BudgetCategoryDTO createCategory(@RequestBody BudgetCategoryCreateRequest request) {
        return accountingFacade.createCategory(request.getName(), request.getColor(), request.getIcon());
    }

    @PutMapping("{categoryId}")
    public BudgetCategoryDTO modifyCategory(@PathVariable long categoryId, @RequestBody BudgetCategoryModifyRequest request) throws CategoryNotFoundException {
        return accountingFacade.modifyCategory(categoryId, request.getName(), request.getColor(), request.getIcon());
    }

    @DeleteMapping("{categoryId}")
    public BudgetCategoryDeleteResponse deleteCategoryById(@PathVariable long categoryId) throws CategoryNotFoundException {
        accountingFacade.deleteCategory(categoryId);
        return new BudgetCategoryDeleteResponse(categoryId);
    }

}
