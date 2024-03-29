package ru.vzotov.accounting.infrastructure.persistence.jpa;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.domain.model.WorkCalendarRepository;
import ru.vzotov.calendar.domain.model.WorkCalendar;
import ru.vzotov.calendar.domain.model.WorkCalendars;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsAll(WorkCalendars.CALENDAR_2019.specialDays());
    }

    @Test
    public void testFind() {
        WorkCalendar calendar = workCalendarRepository.find(WorkCalendars.RUSSIA, LocalDate.of(2020, Month.JANUARY, 1), LocalDate.of(2020, Month.DECEMBER, 31));
        assertThat(calendar.specialDays())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsAll(WorkCalendars.CALENDAR_2020.specialDays());
    }

    @Test
    public void testFindEmpty() {
        WorkCalendar calendar = workCalendarRepository.find(WorkCalendars.RUSSIA, LocalDate.of(2035, Month.FEBRUARY, 1), LocalDate.of(2035, Month.AUGUST, 1));
        assertThat(calendar.specialDays())
                .isNotNull();
    }
}
