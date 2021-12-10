package ru.vzotov.cashreceipt.application.nalogru2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusDescription {
    @JsonProperty("long")
    String longDescription;
    @JsonProperty("short")
    String shortDescription;

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
