package com.snxy.user.agent.domain;

public class UserIdentityTypeKey {
    private Long systemUserId;

    private Integer identityTypeId;

    public Long getSystemUserId() {
        return systemUserId;
    }

    public void setSystemUserId(Long systemUserId) {
        this.systemUserId = systemUserId;
    }

    public Integer getIdentityTypeId() {
        return identityTypeId;
    }

    public void setIdentityTypeId(Integer identityTypeId) {
        this.identityTypeId = identityTypeId;
    }
}