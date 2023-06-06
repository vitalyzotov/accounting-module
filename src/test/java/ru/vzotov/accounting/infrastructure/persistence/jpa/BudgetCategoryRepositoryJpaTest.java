package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.BudgetCategoryRepository;
import ru.vzotov.banking.domain.model.BudgetCategory;
import ru.vzotov.banking.domain.model.BudgetCategoryId;
import ru.vzotov.person.domain.model.PersonId;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class BudgetCategoryRepositoryJpaTest {
    private static final Logger log = LoggerFactory.getLogger(BudgetCategoryRepositoryJpaTest.class);

    private static final PersonId PERSON_ID = new PersonId("c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2");

    @Autowired
    private BudgetCategoryRepository repository;

    @Test
    public void find() {
        BudgetCategory category = repository.find(PERSON_ID, "Прочие расходы");
        log.info("Category loaded {}", category);
        assertThat(category).isNotNull();
    }

    @Test
    public void findById() {
        BudgetCategory category = repository.find(new BudgetCategoryId(781381038049753674L));
        log.info("Category loaded {}", category);
        assertThat(category).isNotNull();
        assertThat(category.name()).isEqualTo("Прочие расходы");
        assertThat(category.color()).isEqualTo(0xFF000000);
        assertThat(category.icon()).isEqualTo("MyIcon");
    }

    @Test
    public void findNotExistent() {
        BudgetCategory category = repository.find(PERSON_ID, "Несуществующая категория");
        assertThat(category).isNull();
    }

    @Test
    public void findAll() {
        List<BudgetCategory> all = repository.findAll(PERSON_ID);
        assertThat(all).isNotNull();
        assertThat(all).isNotEmpty();
    }

    @Test
    public void store() {
        BudgetCategory category = new BudgetCategory(BudgetCategoryId.of("Транспорт"), PERSON_ID, "Транспорт");
        repository.store(category);
    }

}
