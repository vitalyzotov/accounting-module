package ru.vzotov.accounting.infrastructure.persistence.jpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.cashreceipt.domain.model.CheckId;
import ru.vzotov.cashreceipt.domain.model.CheckQRCode;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.domain.model.QRCodeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class QRCodeRepositoryJpaTest {

    @Autowired
    private QRCodeRepository repository;

    @Test
    public void find() {
        CheckQRCode qrCode = repository.find(new CheckId("20180616135500_65624_8710000100313204_110992_2128735201_1"));
        assertThat(qrCode).isNotNull();
    }

    @Test
    public void findByQRCodeData() {
        CheckQRCode notFound = repository.findByQRCodeData(new QRCodeData("t=20180614T1641&s=566.92&fn=8710000100312991&i=21128&fp=2663320648&n=1"));
        assertThat(notFound).isNull();

        CheckQRCode qrCode = repository.findByQRCodeData(new QRCodeData("t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&fp=2128735201&n=1"));
        assertThat(qrCode).isNotNull();
    }

    @Test
    public void store() {
        CheckQRCode qrCode = new CheckQRCode(
                new QRCodeData("t=20190215T145400&s=741.92&fn=9282000100199855&i=493&fp=1237736343&n=1")
        );
        repository.store(qrCode);
    }

    @Test
    public void loadedAt() {
        CheckQRCode code = repository.find(new CheckId("20180616135500_65624_8710000100313204_110992_2128735201_1"));
        assertThat(code.loadedAt()).isEqualTo(OffsetDateTime.of(
                LocalDateTime.of(2018, Month.JUNE, 16, 14, 0, 0), ZoneOffset.UTC
        ));

        code.tryLoading();
        OffsetDateTime newTimestamp = code.loadedAt();
        repository.store(code);
        code = repository.find(new CheckId("20180616135500_65624_8710000100313204_110992_2128735201_1"));
        assertThat(code.loadedAt()).isEqualTo(newTimestamp);
    }

    @Test
    public void findByDate() {
        List<CheckQRCode> list = repository.findByDate(LocalDate.of(2018, Month.JUNE, 16), LocalDate.of(2018, Month.JUNE, 16));
        assertThat(list).isNotEmpty();
    }

    @Test
    public void findWithoutDeals() {
        List<CheckQRCode> list = repository.findWithoutDeals();
        assertThat(list).isNotEmpty()
                .map(CheckQRCode::checkId)
                .contains(new CheckId("20180616135500_65625_8710000100313205_110993_2128735202_1"));
    }
}
