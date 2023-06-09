package ru.vzotov.accounting.interfaces.accounting.rest;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vzotov.cashreceipt.application.events.PreAuthEvent;

@RestController
@RequestMapping("/accounting/config")
@CrossOrigin
public class ConfigController {

    private final ApplicationEventPublisher eventPublisher;

    public ConfigController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PutMapping("/nalog-auth")
    public void authenticateNalogRu(@RequestBody NalogPreAuthRequest auth) {
        //fixme: rework, use any multi-user solution
        if (auth == null || auth.sessionId() == null || auth.refreshToken() == null) {
            throw new IllegalArgumentException();
        }
        this.eventPublisher.publishEvent(new PreAuthEvent(this, auth.sessionId(), auth.refreshToken()));
    }

    public record NalogPreAuthRequest(String sessionId, String refreshToken) {
    }
}
