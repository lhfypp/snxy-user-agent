package com.snxy.user.agent.domain;

public class IdentityType {
    private Integer id;

    private Byte identyId;

    private String identyName;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getIdentyId() {
        return identyId;
    }

    public void setIdentyId(Byte identyId) {
        this.identyId = identyId;
    }

    public String getIdentyName() {
        return identyName;
    }

    public void setIdentyName(String identyName) {
        this.identyName = identyName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}