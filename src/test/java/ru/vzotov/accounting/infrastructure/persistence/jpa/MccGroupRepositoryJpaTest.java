package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.MccGroupRepository;
import ru.vzotov.banking.domain.model.MccGroup;
import ru.vzotov.banking.domain.model.MccGroupId;

import jakarta.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class MccGroupRepositoryJpaTest {

    @Autowired
    private MccGroupRepository mccGroupRepository;

    @Test
    public void findAllDetails() {
        List<MccGroup> all = mccGroupRepository.findAll();
        assertThat(all).isNotEmpty().hasSize(20);
    }

    @Test
    public void findGroupById() {
        MccGroup group = mccGroupRepository.find(new MccGroupId("af7853df-3104-433a-86a3-5950273a7380"));
        assertThat(group)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new MccGroup("af7853df-3104-433a-86a3-5950273a7380", "Авиалинии, авиакомпании"));
    }

}
