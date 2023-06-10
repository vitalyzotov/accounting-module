package ru.vzotov.accounting.interfaces.accounting.rest;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.vzotov.accounting.interfaces.accounting.AccountingApi;
import ru.vzotov.accounting.interfaces.common.CommonApi;
import ru.vzotov.accounting.test.AbstractControllerTest;
import ru.vzotov.cashreceipt.application.nalogru2.ReceiptRepositoryNalogru2;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.time.Month.JUNE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static ru.vzotov.accounting.test.DateUtils.inCurrentZone;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class AccountingControllerTest extends AbstractControllerTest {

    private static final String PERSON_ID = "c483a33e-5e84-4d4c-84fe-4edcb5cc0fd2";

    @MockBean
    private ReceiptRepositoryNalogru2 nalogru;

    @Test
    public void getReceipts() {
        AccountingApi.Receipt.Many body = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .getForObject("/accounting/receipts/?from=2018-06-16&to=2018-06-17", AccountingApi.Receipt.Many.class);
        assertThat(body.receipts()).isNotEmpty();
    }

    @Test
    public void getCodes() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("x-api-version", "2");
        ResponseEntity<AccountingApi.QRCode[]> response = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .exchange(
                        "/accounting/qr/?from=2018-06-16&to=2018-06-17",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        AccountingApi.QRCode[].class
                );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotEmpty();
        AccountingApi.QRCode expected = new AccountingApi.QRCode(
                "20180616135500_65624_8710000100313204_110992_2128735201_1",
                new AccountingApi.QRCodeData(
                        LocalDateTime.of(2018, JUNE, 16, 13, 55, 0),
                        new CommonApi.Money(65624, "RUR"),
                        "8710000100313204",
                        "110992",
                        "2128735201",
                        1L
                ),
                "NEW",
                1L,
                inCurrentZone(LocalDateTime.of(2018, JUNE, 16, 14, 0, 0)),
                PERSON_ID
        );
        assertThat(response.getBody())
                .usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder()
                        .withComparatorForType(
                                Comparator.comparing(a -> a.truncatedTo(ChronoUnit.MILLIS).toInstant()),
                                OffsetDateTime.class
                        )
                        .build())
                .contains(expected);
    }

    @Test
    public void getReceipt() {
        AccountingApi.Receipt.One body = this.restTemplate
                .withBasicAuth(USER, PASSWORD)
                .getForObject("/accounting/receipts/2128735201?t=20180616T1355&s=656.24&fn=8710000100313204&i=110992&n=1", AccountingApi.Receipt.One.class);
        assertThat(body.receipt().items()).hasSize(2);
    }

    @Test
    public void registerReceipt() throws IOException {
        final String qrValue = "t=20180717T1655&s=1350.00&fn=9288000100080483&i=944&fp=2361761706&n=1";
        final QRCodeData qr = new QRCodeData(qrValue);

        try (BufferedReader data = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/receipt017.json"))))) {
            String dataString = data.lines().collect(Collectors.joining(System.lineSeparator()));

            given(this.nalogru.findByQRCodeData(qr)).willReturn(dataString);

            AccountingApi.Receipt.Register request = new AccountingApi.Receipt.Register("t=20180717T1655&s=1350.00&fn=9288000100080483&i=944&fp=2361761706&n=1");
            AccountingApi.Receipt.Ref response = this.restTemplate
                    .withBasicAuth(USER, PASSWORD)
                    .postForObject("/accounting/receipts/", request, AccountingApi.Receipt.Ref.class);
            assertThat(response).isNotNull();
            assertThat(response.id()).isNotNull();
            verify(this.nalogru, times(0)).findByQRCodeData(any(QRCodeData.class));
            verifyNoMoreInteractions(this.nalogru);
        }
    }
}
