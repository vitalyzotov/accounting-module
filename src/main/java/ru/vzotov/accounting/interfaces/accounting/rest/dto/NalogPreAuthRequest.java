package ru.vzotov.accounting.interfaces.accounting.rest.dto;

public class NalogPreAuthRequest {
    private String sessionId;
    private String refreshToken;

    public NalogPreAuthRequest() {
    }

    public NalogPreAuthRequest(String sessionId, String refreshToken) {
        this.sessionId = sessionId;
        this.refreshToken = refreshToken;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
