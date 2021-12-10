package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.cashreceipt.domain.model.Check;
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.domain.model.ReceiptRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.List;

public class ReceiptRepositoryJpa extends JpaRepository implements ReceiptRepository {

    ReceiptRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public Check find(CheckId id) {
        try {
            return em.createQuery("from Check where checkId = :id"
                    , Check.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Check findByQRCodeData(QRCodeData data) {
        try {
            return em.createQuery("from Check where dateTime = :dateTime" +
                            " AND products.totalSum = :totalSum" +
                            " AND fiscalInfo.fiscalDriveNumber = :fiscalDriveNumber" +
                            " AND fiscalInfo.fiscalDocumentNumber = :fiscalDocumentNumber" +
                            " AND fiscalInfo.fiscalSign.value = :fiscalSign" +
                            " AND operationType = :operationType"
                    , Check.class)
                    .setParameter("dateTime", data.dateTime().value())
                    .setParameter("totalSum", data.totalSum())
                    .setParameter("fiscalDriveNumber", data.fiscalDriveNumber())
                    .setParameter("fiscalDocumentNumber", data.fiscalDocumentNumber())
                    .setParameter("fiscalSign", data.fiscalSign().value())
                    .setParameter("operationType", data.operationType())
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void store(Check receipt) {
        if (hasId(receipt, "id")) {
            em.detach(receipt);
            em.merge(receipt);
            em.flush();
        } else {
            em.persist(receipt);
        }
    }

    @Override
    public List<Check> findByDate(LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("from Check where dateTime >= :fromDate AND dateTime < :toDatePlusOneDay", Check.class)
                .setParameter("fromDate", fromDate.atStartOfDay())
                .setParameter("toDatePlusOneDay", toDate.plusDays(1).atStartOfDay())
                .getResultList();
    }

    @Override
    public long countByDate(LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("select count(c) from Check c where c.dateTime >= :fromDate AND c.dateTime < :toDatePlusOneDay", Long.class)
                .setParameter("fromDate", fromDate.atStartOfDay())
                .setParameter("toDatePlusOneDay", toDate.plusDays(1).atStartOfDay())
                .getSingleResult();
    }

    public Check findOldest() {
        try {
            return em.createQuery("from Check order by dateTime asc", Check.class).setMaxResults(1).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
