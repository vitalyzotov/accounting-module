package ru.vzotov.accounting.application.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.WithMockPersonUser;
import ru.vzotov.cashreceipt.application.ReceiptNotFoundException;
import ru.vzotov.cashreceipt.application.ReceiptRegistrationService;
import ru.vzotov.cashreceipt.application.nalogru2.ReceiptRepositoryNalogru2;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.domain.model.ReceiptId;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ReceiptRegistrationServiceImplTest {
    private static final Logger log = LoggerFactory.getLogger(ReceiptRegistrationServiceImplTest.class);
    private static final String PERSON_ID = "c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2";

    @MockBean
    private ReceiptRepositoryNalogru2 nalogru;

    @Autowired
    private ReceiptRegistrationService service;

    @WithMockPersonUser(person = PERSON_ID)
    @Test
    public void registerReceipt() throws ReceiptNotFoundException, IOException {
        final QRCodeData qr = new QRCodeData("t=20180629T002100&s=1109.00&fn=9286000100174236&i=6079&fp=3461764748&n=1");
        final QRCodeData qrNonExistent = new QRCodeData("t=20180717T1708&s=150.00&fn=9288000100080294&i=2056&fp=2024263777&n=2");
        try (BufferedReader data = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/receipt010.json"))))) {
            String dataString = data.lines().collect(Collectors.joining(System.lineSeparator()));

            given(this.nalogru.findByQRCodeData(qr)).willReturn(dataString);
            given(this.nalogru.findByQRCodeData(qrNonExistent)).willReturn(null);

            // ?????????????????? ??????????????????????
            ReceiptId receiptId = service.register(qr);
            assertThat(receiptId).isNotNull();

            // ?????????????????? ??????????????????????
            ReceiptId receiptIdRepeat = service.register(qr);
            assertThat(receiptId).isEqualTo(receiptIdRepeat);

            // ?????????????????????? ????????, ???????????????? ???? ???????????????? ?????????????????????? ?? ??????????????????
            service.register(qrNonExistent);

            verifyZeroInteractions(this.nalogru);
        }
    }
}
