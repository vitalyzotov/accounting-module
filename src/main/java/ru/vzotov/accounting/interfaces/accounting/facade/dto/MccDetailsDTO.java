package ru.vzotov.accounting.interfaces.accounting.facade.dto;

public class MccDetailsDTO {
    private String mcc;
    private String name;
    private String groupId;

    public MccDetailsDTO() {
    }

    public MccDetailsDTO(String mcc, String name, String groupId) {
        this.mcc = mcc;
        this.name = name;
        this.groupId = groupId;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
