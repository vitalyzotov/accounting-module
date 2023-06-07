package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.accounting.domain.model.WorkCalendarRepository;
import ru.vzotov.calendar.domain.model.SpecialDay;
import ru.vzotov.calendar.domain.model.WorkCalendar;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;

public class WorkCalendarRepositoryJpa extends JpaRepository implements WorkCalendarRepository {

    WorkCalendarRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public WorkCalendar find(String location, LocalDate from, LocalDate to) {
        return new WorkCalendar(from, to,
                location,
                em.createQuery("from SpecialDay where location = :location and day >= :dateFrom and day <= :dateTo", SpecialDay.class)
                        .setParameter("dateFrom", from)
                        .setParameter("dateTo", to)
                        .setParameter("location", location)
                        .getResultList());
    }

    @Override
    public void store(WorkCalendar calendar) {
        calendar.specialDays().forEach(em::persist);
    }
}
