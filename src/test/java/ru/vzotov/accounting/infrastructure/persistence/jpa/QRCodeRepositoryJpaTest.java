package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.cashreceipt.domain.model.QRCode;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.domain.model.QRCodeRepository;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;
import ru.vzotov.person.domain.model.PersonId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.vzotov.accounting.test.DateUtils.inCurrentZone;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class QRCodeRepositoryJpaTest {

    private static final PersonId PERSON_ID = new PersonId("c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2");

    @Autowired
    private QRCodeRepository repository;

    @Test
    public void find() {
        QRCode qrCode = repository.find(new ReceiptId("20180616135500_65624_8710000100313204_110992_2128735201_1"));
        assertThat(qrCode).isNotNull();
    }

    @Test
    public void findByQRCodeData() {
        QRCode notFound = repository.findByQRCodeData(new QRCodeData("t=20180614T1641&s=566.92&fn=8710000100312991&i=21128&fp=2663320648&n=1"));
        assertThat(notFound).isNull();

        QRCode qrCode = repository.findByQRCodeData(new QRCodeData("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1"));
        assertThat(qrCode).isNotNull();
    }

    @Test
    public void store() {
        QRCode qrCode = new QRCode(
                new QRCodeData("t=20190215T145400&s=741.92&fn=9282000100199855&i=493&fp=1237736343&n=1"),
                PERSON_ID
        );
        repository.store(qrCode);
    }

    @Test
    public void loadedAt() {
        QRCode code = repository.find(new ReceiptId("20180616135500_65624_8710000100313204_110992_2128735201_1"));
        assertThat(code.loadedAt()).isEqualTo(inCurrentZone(LocalDateTime.of(2018, Month.JUNE, 16, 14, 0, 0)));

        code.tryLoading();
        OffsetDateTime newTimestamp = code.loadedAt();
        repository.store(code);
        code = repository.find(new ReceiptId("20180616135500_65624_8710000100313204_110992_2128735201_1"));
        assertThat(code.loadedAt()).isEqualTo(newTimestamp);
    }

    @Test
    public void findByDate() {
        List<QRCode> list = repository.findByDate(Collections.singleton(PERSON_ID),
                LocalDate.of(2018, Month.JUNE, 16), LocalDate.of(2018, Month.JUNE, 16));
        assertThat(list).isNotEmpty();
    }

    @Test
    public void findWithoutDeals() {
        List<QRCode> list = repository.findWithoutDeals();
        assertThat(list).isNotEmpty()
                .map(QRCode::receiptId)
                .contains(new ReceiptId("20180616135500_65625_8710000100313205_110993_2128735202_1"));
    }
}
