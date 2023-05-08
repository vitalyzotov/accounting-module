package ru.vzotov.accounting.interfaces.contacts.facade.dto;

public class ContactDataDTO {

    private String mimeType;

    private String value;

    private String type;

    public ContactDataDTO() {
    }

    public ContactDataDTO(String mimeType, String value, String type) {
        this.mimeType = mimeType;
        this.value = value;
        this.type = type;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
