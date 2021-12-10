package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.CheckQRCode;
import ru.vzotov.cashreceipt.domain.model.CheckState;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.domain.model.QRCodeRepository;
import ru.vzotov.cashreceipt.domain.model.QRCodeDateTime;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class QRCodeRepositoryJpa extends JpaRepository implements QRCodeRepository {

    QRCodeRepositoryJpa(EntityManager em) {
        super(em);
    }

    @Override
    public CheckQRCode find(CheckId id) {
        try {
            return em.createQuery("from CheckQRCode where checkId=:checkId"
                    , CheckQRCode.class)
                    .setParameter("checkId", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public CheckQRCode findByQRCodeData(QRCodeData data) {
        try {
            return em.createQuery("from CheckQRCode where code.dateTime = :dateTime" +
                            " AND code.totalSum = :totalSum" +
                            " AND code.fiscalDriveNumber = :fiscalDriveNumber" +
                            " AND code.fiscalDocumentNumber = :fiscalDocumentNumber" +
                            " AND code.fiscalSign.value = :fiscalSign" +
                            " AND code.operationType = :operationType"
                    , CheckQRCode.class)
                    .setParameter("dateTime", data.dateTime())
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
    public void store(CheckQRCode qrCode) {
        em.persist(qrCode);
    }

    @Override
    public List<CheckQRCode> findByDate(LocalDate fromDate, LocalDate toDate) {
        final ZoneId zoneId = ZoneId.systemDefault().normalized();
        return em.createQuery("from CheckQRCode where code.dateTime >= :fromDate AND code.dateTime < :toDatePlusOneDay", CheckQRCode.class)
                .setParameter("fromDate", new QRCodeDateTime(fromDate.atStartOfDay()))
                .setParameter("toDatePlusOneDay", new QRCodeDateTime(toDate.plusDays(1).atStartOfDay()))
                .getResultList();
    }

    @Override
    public List<CheckQRCode> findAllInState(CheckState state) {
        return em.createQuery("from CheckQRCode where state = :state", CheckQRCode.class)
                .setParameter("state", state)
                .getResultList();
    }

    @Override
    public List<CheckQRCode> findWithoutDeals() {
        return em.createNamedQuery("receipts-without-deals", CheckQRCode.class)
                .getResultList();
    }
}
