package ru.vzotov.cashreceipt.application.events;

import org.springframework.context.ApplicationEvent;

public class PreAuthEvent extends ApplicationEvent {
    private final String sessionId;
    private final String refreshToken;

    public PreAuthEvent(Object source, String sessionId, String refreshToken) {
        super(source);
        this.sessionId = sessionId;
        this.refreshToken = refreshToken;
    }

    public String sessionId() {
        return sessionId;
    }

    public String refreshToken() {
        return refreshToken;
    }
}
