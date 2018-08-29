package com.snxy.user.agent.domain;

public class UserIdentityType extends UserIdentityTypeKey {
    private Byte isActive;

    public Byte getIsActive() {
        return isActive;
    }

    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }
}