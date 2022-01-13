package ru.vzotov.accounting.infrastructure.persistence.jpa;

import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.cashreceipt.domain.model.QRCode;
import ru.vzotov.cashreceipt.domain.model.ReceiptState;
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
    public QRCode find(ReceiptId id) {
        try {
            return em.createQuery("from QRCode where receiptId=:receiptId"
                    , QRCode.class)
                    .setParameter("receiptId", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public QRCode findByQRCodeData(QRCodeData data) {
        try {
            return em.createQuery("from QRCode where data.dateTime = :dateTime" +
                            " AND data.totalSum = :totalSum" +
                            " AND data.fiscalDriveNumber = :fiscalDriveNumber" +
                            " AND data.fiscalDocumentNumber = :fiscalDocumentNumber" +
                            " AND data.fiscalSign.value = :fiscalSign" +
                            " AND data.operationType = :operationType"
                    , QRCode.class)
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
    public void store(QRCode qrCode) {
        em.persist(qrCode);
    }

    @Override
    public List<QRCode> findByDate(LocalDate fromDate, LocalDate toDate) {
        final ZoneId zoneId = ZoneId.systemDefault().normalized();
        return em.createQuery("from QRCode where data.dateTime >= :fromDate AND data.dateTime < :toDatePlusOneDay", QRCode.class)
                .setParameter("fromDate", new QRCodeDateTime(fromDate.atStartOfDay()))
                .setParameter("toDatePlusOneDay", new QRCodeDateTime(toDate.plusDays(1).atStartOfDay()))
                .getResultList();
    }

    @Override
    public List<QRCode> findAllInState(ReceiptState state) {
        return em.createQuery("from QRCode where state = :state", QRCode.class)
                .setParameter("state", state)
                .getResultList();
    }

    @Override
    public List<QRCode> findWithoutDeals() {
        return em.createNamedQuery("receipts-without-deals", QRCode.class)
                .getResultList();
    }
}
