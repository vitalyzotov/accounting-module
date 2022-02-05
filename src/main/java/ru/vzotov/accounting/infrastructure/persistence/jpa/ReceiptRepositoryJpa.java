package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.cashreceipt.domain.model.Receipt;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.domain.model.ReceiptRepository;
import ru.vzotov.person.domain.model.PersonId;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class ReceiptRepositoryJpa extends JpaRepository implements ReceiptRepository {

    ReceiptRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public Receipt find(ReceiptId id) {
        try {
            return em.createQuery("from Receipt where receiptId = :id"
                    , Receipt.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Receipt findByQRCodeData(QRCodeData data) {
        try {
            return em.createQuery("from Receipt where dateTime = :dateTime" +
                            " AND products.totalSum = :totalSum" +
                            " AND fiscalInfo.fiscalDriveNumber = :fiscalDriveNumber" +
                            " AND fiscalInfo.fiscalDocumentNumber = :fiscalDocumentNumber" +
                            " AND fiscalInfo.fiscalSign.value = :fiscalSign" +
                            " AND operationType = :operationType"
                    , Receipt.class)
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
    public void store(Receipt receipt) {
        if (hasId(receipt, "id")) {
            em.detach(receipt);
            em.merge(receipt);
            em.flush();
        } else {
            em.persist(receipt);
        }
    }

    @Override
    public List<Receipt> findByDate(Collection<PersonId> owners, LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("from Receipt where owner in (:owners) and dateTime >= :fromDate and dateTime < :toDatePlusOneDay", Receipt.class)
                .setParameter("owners", owners)
                .setParameter("fromDate", fromDate.atStartOfDay())
                .setParameter("toDatePlusOneDay", toDate.plusDays(1).atStartOfDay())
                .getResultList();
    }

    @Override
    public long countByDate(Collection<PersonId> owners, LocalDate fromDate, LocalDate toDate) {
        return em.createQuery("select count(c) from Receipt c where c.owner in (:owners) and c.dateTime >= :fromDate AND c.dateTime < :toDatePlusOneDay", Long.class)
                .setParameter("owners", owners)
                .setParameter("fromDate", fromDate.atStartOfDay())
                .setParameter("toDatePlusOneDay", toDate.plusDays(1).atStartOfDay())
                .getSingleResult();
    }

    public Receipt findOldest(Collection<PersonId> owners) {
        try {
            return em.createQuery("from Receipt where owner in (:owners) order by dateTime asc", Receipt.class)
                    .setParameter("owners", owners)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
