package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.MccDetailsRepository;
import ru.vzotov.banking.domain.model.MccCode;
import ru.vzotov.banking.domain.model.MccDetails;
import ru.vzotov.banking.domain.model.MccGroup;
import ru.vzotov.banking.domain.model.MccGroupId;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class MccDetailsRepositoryJpaTest {

    @Autowired
    private MccDetailsRepository mccDetailsRepository;

    @Test
    public void findAllDetails() {
        List<MccDetails> all = mccDetailsRepository.findAll();
        assertThat(all).isNotEmpty().hasSize(1021);
    }

    @Test
    public void findDetailsByGroup() {
        List<MccDetails> group = mccDetailsRepository.findByGroup(new MccGroupId("af7853df-3104-433a-86a3-5950273a7380"));
        assertThat(group).isNotEmpty().hasSize(289);
    }

    @Test
    public void findDetailsByCode() {
        MccDetails details = mccDetailsRepository.find(new MccCode("0742"));
        assertThat(details).isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("group.id")
                .isEqualTo(new MccDetails(new MccCode("0742"), "Ветеринарные услуги",
                        new MccGroup(new MccGroupId("4fce2602-d6d3-4bec-a7ff-df70cb767c2d"), "Контрактные услуги")));
    }

}
