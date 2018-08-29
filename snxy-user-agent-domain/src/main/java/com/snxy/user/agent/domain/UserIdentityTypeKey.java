package com.snxy.user.agent.domain;

public class UserIdentityTypeKey {
    private Long systemUserId;

    private Integer identyTypeId;

    public Long getSystemUserId() {
        return systemUserId;
    }

    public void setSystemUserId(Long systemUserId) {
        this.systemUserId = systemUserId;
    }

    public Integer getIdentyTypeId() {
        return identyTypeId;
    }

    public void setIdentyTypeId(Integer identyTypeId) {
        this.identyTypeId = identyTypeId;
    }
}