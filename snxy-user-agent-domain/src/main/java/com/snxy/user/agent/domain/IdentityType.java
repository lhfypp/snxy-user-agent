package com.snxy.user.agent.domain;

public class IdentityType {
    private Integer id;

    private Byte identityId;

    private String identityName;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getIdentityId() {
        return identityId;
    }

    public void setIdentityId(Byte identityId) {
        this.identityId = identityId;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdentityType)) return false;

        IdentityType that = (IdentityType) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (identityId != null ? !identityId.equals(that.identityId) : that.identityId != null) return false;
        if (identityName != null ? !identityName.equals(that.identityName) : that.identityName != null) return false;
        return remark != null ? remark.equals(that.remark) : that.remark == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (identityId != null ? identityId.hashCode() : 0);
        result = 31 * result + (identityName != null ? identityName.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IdentityType{" +
                "id=" + id +
                ", identityId=" + identityId +
                ", identityName='" + identityName + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}