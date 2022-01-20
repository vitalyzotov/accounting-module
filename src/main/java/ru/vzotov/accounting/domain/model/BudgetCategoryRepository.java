package ru.vzotov.accounting.domain.model;

import ru.vzotov.banking.domain.model.BudgetCategory;
import ru.vzotov.banking.domain.model.BudgetCategoryId;
import ru.vzotov.person.domain.model.PersonId;

import java.util.List;

public interface BudgetCategoryRepository {

    BudgetCategory find(BudgetCategoryId id);

    BudgetCategory find(PersonId owner, String name);

    List<BudgetCategory> findAll(PersonId owner);

    void store(BudgetCategory category);

    void delete(BudgetCategory category);
}
