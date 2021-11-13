package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.MccGroupRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.banking.domain.model.MccGroup;
import ru.vzotov.banking.domain.model.MccGroupId;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
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
        assertThat(group).isEqualToIgnoringGivenFields(new MccGroup("af7853df-3104-433a-86a3-5950273a7380", "Авиалинии, авиакомпании"), "id");
    }

}
