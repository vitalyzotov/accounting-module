package ru.vzotov.cashreceipt.application.nalogru2;

public class AuthRequest {
    String client_secret;
    String inn;
    String password;

    public AuthRequest() {
    }

    public AuthRequest(String client_secret, String inn, String password) {
        this.client_secret = client_secret;
        this.inn = inn;
        this.password = password;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
