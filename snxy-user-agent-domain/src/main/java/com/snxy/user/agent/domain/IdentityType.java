package com.snxy.user.agent.domain;

import lombok.Data;

import java.util.Date;
@Data
public class IdentityType {
    private Integer id;

    private String identityName;

    private String remark;

    private Boolean isDelete;

    private Date gmtCreate;

    private Date gmtModified;

}