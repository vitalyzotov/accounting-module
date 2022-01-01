package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.util.Objects;

public class PosTerminalDTO {
    private String terminalId;
    private String country;
    private String city;
    private String street;
    private String merchant;

    public PosTerminalDTO() {
    }

    public PosTerminalDTO(String terminalId, String country, String city, String street, String merchant) {
        this.terminalId = terminalId;
        this.country = country;
        this.city = city;
        this.street = street;
        this.merchant = merchant;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PosTerminalDTO that = (PosTerminalDTO) o;

        if (!Objects.equals(terminalId, that.terminalId)) return false;
        if (!Objects.equals(country, that.country)) return false;
        if (!Objects.equals(city, that.city)) return false;
        if (!Objects.equals(street, that.street)) return false;
        return Objects.equals(merchant, that.merchant);
    }

    @Override
    public int hashCode() {
        int result = terminalId != null ? terminalId.hashCode() : 0;
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (merchant != null ? merchant.hashCode() : 0);
        return result;
    }
}
