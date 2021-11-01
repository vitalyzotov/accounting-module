package ru.vzotov.accounting.domain.model;

import ru.vzotov.banking.domain.model.BudgetCategory;
import ru.vzotov.banking.domain.model.BudgetCategoryId;

import java.util.List;

public interface BudgetCategoryRepository {

    BudgetCategory find(BudgetCategoryId id);

    BudgetCategory find(String name);

    List<BudgetCategory> findAll();

    void store(BudgetCategory category);

    void delete(BudgetCategory category);
}
