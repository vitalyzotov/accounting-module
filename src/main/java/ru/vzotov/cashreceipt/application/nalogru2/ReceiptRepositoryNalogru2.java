package ru.vzotov.cashreceipt.application.nalogru2;

import ru.vzotov.cashreceipt.domain.model.QRCodeData;
import ru.vzotov.cashreceipt.application.events.PreAuthEvent;
import ru.vzotov.cashreceipt.application.impl.LoggingRequestInterceptor;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

/**
 * Класс для работы с сервисом проверки чеков налоговой, начиная с августа 2020 года.
 * С 03.08.2020 старый сервис перестал работать и начал выдавать ошибку 426 Upgrade required.
 */
@Repository
public class ReceiptRepositoryNalogru2 {

    private static final Logger log = LoggerFactory.getLogger(ReceiptRepositoryNalogru2.class);

    @Value("${nalogru2.address:https://irkkt-mobile.nalog.ru:8888}")
    private String address;

    @Value("${nalogru2.clientVersion:2.9.0}")
    private String clientVersion;

    @Value("${nalogru2.deviceId}")
    private String deviceId;

    @Value("${nalogru2.clientSecret}")
    private String clientSecret;

    @Value("${nalogru2.deviceOs:Android}")
    private String deviceOs;

    @Value("${nalogru2.userAgent:okhttp/4.2.2}")
    private String userAgent;

    @Value("${nalogru2.username}")
    private String username;

    @Value("${nalogru2.password}")
    private String password;

    /**
     * Идентификатор сессии.
     */
    private String sessionId;

    private String refreshToken;

    /**
     * Создается при аутентификации
     */
    private RestTemplate restTemplate;

    private final RestTemplateBuilder restTemplateBuilder;

    public ReceiptRepositoryNalogru2(RestTemplateBuilder restTemplateBuilder
                                     //        , @Qualifier("extraInterceptors") List<ClientHttpRequestInterceptor> extraInterceptors
    ) {
        this.restTemplateBuilder = restTemplateBuilder;
        //this.extraInterceptors = extraInterceptors;
    }

    public String findByQRCodeData(QRCodeData data) {
        try {
            log.info("Find in nalog.ru by qr code {}", data);
            Ticket ticket = findTicket(data);
            return getTicketInfoAsString(ticket);
        } catch (IllegalArgumentException e) {
            log.error("Unable to load receipt from nalog.ru", e);
        } catch (RestClientException e) {
            log.error("Unknown rest client error", e);
        }

        return null;
    }

    /**
     * Создает сессию с использованием заданных идентификаторов.
     * Нужен на случаи сбоев на стороне налоговой.
     *
     * @param sessionId идентификатор сессии
     * @param refreshToken токен для обновления сессии
     */
    public void authenticatePreemptively(String sessionId, String refreshToken) {
        log.info("Authenticate preemptively by using session {} and refresh token {}", sessionId, refreshToken);
        this.sessionId = sessionId;
        this.refreshToken = refreshToken;
        this.restTemplate = buildRestTemplate();
    }

    @EventListener
    public void onPreAuth(PreAuthEvent auth) {
        authenticatePreemptively(auth.sessionId(), auth.refreshToken());
    }

    private RestTemplate buildRestTemplate() {
        final RestTemplate r = restTemplateBuilder
                .additionalInterceptors((request, body, execution) -> {
                    final HttpHeaders headers = request.getHeaders();

                    headers.add("ClientVersion", clientVersion);
                    headers.add("Device-Id", deviceId);
                    headers.add("Device-OS", deviceOs);
                    headers.add("sessionId", sessionId);
                    headers.add("User-Agent", userAgent);

                    return execution.execute(request, body);
                })
                .additionalInterceptors(new LoggingRequestInterceptor())
                .build();
        r.setRequestFactory(new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory()));
        return r;
    }

    private RestTemplate getRestTemplate() {
        if (this.restTemplate == null) {
            // создаем RestTemplate для аутентификации
            final RestTemplateBuilder builder = restTemplateBuilder
                    .additionalInterceptors((ClientHttpRequestInterceptor) (request, body, execution) -> {
                        final HttpHeaders headers = request.getHeaders();

                        headers.add("ClientVersion", this.clientVersion);
                        headers.add("Device-Id", deviceId);
                        headers.add("Device-OS", deviceOs);
                        headers.add("User-Agent", userAgent);

                        return execution.execute(request, body);
                    });
            RestTemplate r = builder.build();

            final String url = address + "/v2/mobile/users/lkfl/auth";
            final AuthRequest authRequest = new AuthRequest(clientSecret, username, password);
            final ResponseEntity<AuthResponse> response = r.postForEntity(url, authRequest, AuthResponse.class);
            Validate.isTrue(response.getStatusCode().is2xxSuccessful(), "Not successful auth");

            final AuthResponse session = response.getBody();
            this.sessionId = session.sessionId;
            this.refreshToken = session.refresh_token;
            this.restTemplate = buildRestTemplate();
        }
        return this.restTemplate;
    }

    private void resetSession() {
        this.restTemplate = null;
        this.sessionId = null;
        this.refreshToken = null;
    }

    private Ticket findTicket(QRCodeData data) {
        final String url = address + "/v2/ticket";
        final TicketRequest request = new TicketRequest(data.toString());
        final ResponseEntity<Ticket> response = doInSession(() ->
                getRestTemplate().postForEntity(url, request, Ticket.class));
        Validate.isTrue(response.getStatusCode().is2xxSuccessful(), "Ticket not found");
        return response.getBody();
    }

    private String getTicketInfoAsString(Ticket ticket) {
        final String url = address + "/v2/tickets/{ticketId}";
        final ResponseEntity<String> response = doInSession(() ->
                getRestTemplate().getForEntity(url, String.class, ticket.id));
        Validate.isTrue(response.getStatusCode().is2xxSuccessful(), "Ticket info not loaded");
        return response.getBody();
    }

    private <T> ResponseEntity<T> doInSession(Supplier<ResponseEntity<T>> op) {
        ResponseEntity<T> response = null;

        for (int i = 0; i <= 1; i++) {
            try {
                response = op.get();
                if (HttpStatus.UNAUTHORIZED.equals(response.getStatusCode())) {
                    resetSession();
                } else {
                    break;
                }
            } catch (HttpClientErrorException e) {
                if(HttpStatus.UNAUTHORIZED.equals(e.getStatusCode())) {
                    resetSession();
                } else {
                    throw e;
                }
            }
        }

        return response;
    }
}
