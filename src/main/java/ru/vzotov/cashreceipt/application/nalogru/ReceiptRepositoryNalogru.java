package ru.vzotov.cashreceipt.application.nalogru;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ReceiptRepositoryNalogru {

    private static final Logger log = LoggerFactory.getLogger(ReceiptRepositoryNalogru.class);
    private static final String MOSCOW_ZONE_ID = "Europe/Moscow";
    private static final ZoneId MOSCOW_ZONE = ZoneId.of(MOSCOW_ZONE_ID);

    @Value("${nalogru.address}")
    private String address;

    private final RestTemplate restTemplate;

    private LocalDate limitRestrictionDate;

    public ReceiptRepositoryNalogru(RestTemplateBuilder restTemplateBuilder,
                                    @Value("${nalogru.username}") String username, @Value("${nalogru.password}") String password) {
        this.restTemplate = restTemplateBuilder
                .basicAuthentication(username, password)
                .additionalInterceptors((request, body, execution) -> {
                    request.getHeaders().add("device-os", "");
                    request.getHeaders().add("device-id", "");
                    return execution.execute(request, body);
                })
                .build();
    }

    private void verifyQRCodeExists(QRCodeData data) {
        //final String url = address + "/v1/inns/*/kkts/*/fss/{fn}/tickets/{i}?fiscalSign={fp}&sendToEmail=no";
        final String url = address + "/v1/ofds/*/inns/*/fss/{fn}/operations/{n}/tickets/{i}?fiscalSign={fp}&date={date}&sum={sum}";

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("fn", data.fiscalDriveNumber());
        parameters.put("n", data.operationType());
        parameters.put("i", data.fiscalDocumentNumber());
        parameters.put("fp", data.fiscalSign().value());
        parameters.put("date", data.dateTime().toISOString());
        parameters.put("sum", data.totalSum().rawAmount());
        ResponseEntity<Void> response;
        try {
            response = restTemplate.getForEntity(url, Void.class, parameters);
            if (response.getStatusCode().is4xxClientError()) {
                throw new RestClientException("Error " + response.getStatusCodeValue());
            }
            final Void ignored = response.getBody();
        } catch (RestClientException e) {
            log.error("Rest client error", e);
            throw e;
        }
    }

    private void verifyLimits() {
        final LocalDate today = LocalDate.now(MOSCOW_ZONE);
        if (limitRestrictionDate != null && !today.isAfter(limitRestrictionDate)) {
            throw new RestClientException("Request is restricted by daily limits");
        }
    }


    public String findByQRCodeData(QRCodeData data) {
        final LocalDate now = LocalDate.now(MOSCOW_ZONE);
        try {
            verifyQRCodeExists(data);
        } catch (RestClientException e) {
            log.error("QR code doesn't exist");
            return null;
        }

        try {
            verifyLimits();
        } catch (RestClientException e) {
            log.error("Unable to load receipt from nalog.ru due to daily limits. QR code {}", data);
            return null;
        }

        final String url = address + "/v1/inns/*/kkts/*/fss/{fn}/tickets/{i}?fiscalSign={fp}&sendToEmail=no";

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("fn", data.fiscalDriveNumber());
        parameters.put("i", data.fiscalDocumentNumber());
        parameters.put("fp", data.fiscalSign().value());

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, parameters);

            switch (response.getStatusCode()) {
                case ACCEPTED:
                    log.info("Receipt request accepted, wait for nalog.ru to prepare data");
                    break;

                case NOT_ACCEPTABLE:
                    log.info("Got 406 Not Acceptable from nalog.ru");
                    break;

                case OK:
                    final String receiptJson = response.getBody();
                    log.info(receiptJson);
                    return receiptJson;

                default:
                    log.info("Receipt not loaded. Try loading again later. HTTP Status {}", response.getStatusCodeValue());
                    break;
            }
        } catch (HttpStatusCodeException e) {
            final HttpStatus status = e.getStatusCode();
            if (HttpStatus.PAYMENT_REQUIRED.equals(status)) {
                limitRestrictionDate = now;
            }
        } catch (RestClientException e) {
            log.error("Unknown rest client error", e);
        }

        return null;
    }
}
