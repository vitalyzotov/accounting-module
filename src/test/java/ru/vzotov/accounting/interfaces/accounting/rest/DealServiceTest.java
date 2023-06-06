package ru.vzotov.accounting.interfaces.accounting.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import ru.vzotov.accounting.test.AbstractControllerTest;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.domain.model.QRCodeDateTime;
import ru.vzotov.cashreceipt.domain.model.ReceiptOperationType;
import ru.vzotov.domain.model.Money;
import ru.vzotov.fiscal.FiscalSign;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@ActiveProfiles("test")
public class DealServiceTest extends AbstractControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void whenRegisteringQR() {
        QRCodeData qr = new QRCodeData(
                new QRCodeDateTime(LocalDateTime.of(2021, 12, 7, 17, 44)),
                Money.rubles(1745.79d),
                "9280440301422813",
                "2732",
                new FiscalSign("0385604187"),
                ReceiptOperationType.INCOME
        );

        Map<String, String> data = new HashMap<>();
        data.put("qrcode", qr.toString());

        RestAssured.given()
                .auth().basic(USER, PASSWORD)
                .contentType(ContentType.JSON)
                .body(data).
                when()
                .post("/accounting/receipts/").
                then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("id", notNullValue())
        ;
    }
}
