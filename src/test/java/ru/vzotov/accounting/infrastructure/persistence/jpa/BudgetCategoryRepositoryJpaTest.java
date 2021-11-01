package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.BudgetCategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.banking.domain.model.BudgetCategory;
import ru.vzotov.banking.domain.model.BudgetCategoryId;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(JpaConfig.class)
@SpringBootTest
@Transactional
public class BudgetCategoryRepositoryJpaTest {
    private static final Logger log = LoggerFactory.getLogger(BudgetCategoryRepositoryJpaTest.class);

    @Autowired
    private BudgetCategoryRepository repository;

    @Test
    public void find() {
        BudgetCategory category = repository.find("Прочие расходы");
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
        BudgetCategory category = repository.find("Несуществующая категория");
        assertThat(category).isNull();
    }

    @Test
    public void findAll() {
        List<BudgetCategory> all = repository.findAll();
        assertThat(all).isNotNull();
        assertThat(all).isNotEmpty();
    }

    @Test
    public void store() {
        BudgetCategory category = new BudgetCategory(BudgetCategoryId.of("Транспорт"), "Транспорт");
        repository.store(category);
    }

}
