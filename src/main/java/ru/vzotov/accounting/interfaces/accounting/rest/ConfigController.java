package ru.vzotov.accounting.interfaces.accounting.rest;

import ru.vzotov.cashreceipt.application.events.PreAuthEvent;
import ru.vzotov.accounting.interfaces.accounting.rest.dto.NalogPreAuthRequest;
import org.apache.commons.lang.Validate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        Validate.notNull(auth);
        Validate.notNull(auth.getSessionId());
        Validate.notNull(auth.getRefreshToken());
        this.eventPublisher.publishEvent(new PreAuthEvent(this, auth.getSessionId(), auth.getRefreshToken()));
    }
}
