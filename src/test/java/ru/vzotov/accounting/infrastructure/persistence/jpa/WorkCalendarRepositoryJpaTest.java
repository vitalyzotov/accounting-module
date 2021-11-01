package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.WorkCalendarRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vzotov.calendar.domain.model.WorkCalendar;
import ru.vzotov.calendar.domain.model.WorkCalendars;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({DatasourceConfig.class, JpaConfig.class})
@Transactional
public class WorkCalendarRepositoryJpaTest {
    private static final Logger log = LoggerFactory.getLogger(WorkCalendarRepositoryJpaTest.class);

    @Autowired
    private WorkCalendarRepository workCalendarRepository;

    @Test
    public void testStore() {
        workCalendarRepository.store(WorkCalendars.CALENDAR_2019);
        WorkCalendar calendar = workCalendarRepository.find(WorkCalendars.RUSSIA, LocalDate.of(2019, Month.JANUARY, 1), LocalDate.of(2019, Month.DECEMBER, 31));
        assertThat(calendar.specialDays())
                .usingElementComparatorIgnoringFields("id")
                .containsAll(WorkCalendars.CALENDAR_2019.specialDays());
    }

    @Test
    public void testFind() {
        WorkCalendar calendar = workCalendarRepository.find(WorkCalendars.RUSSIA, LocalDate.of(2020, Month.JANUARY, 1), LocalDate.of(2020, Month.DECEMBER, 31));
        assertThat(calendar.specialDays())
                .usingElementComparatorIgnoringFields("id")
                .containsAll(WorkCalendars.CALENDAR_2020.specialDays());
    }

}
