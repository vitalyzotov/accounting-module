package ru.vzotov.accounting.interfaces.accounting.facade.dto;

public class MccGroupDTO {
    private String groupId;
    private String name;

    public MccGroupDTO() {
    }

    public MccGroupDTO(String groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
