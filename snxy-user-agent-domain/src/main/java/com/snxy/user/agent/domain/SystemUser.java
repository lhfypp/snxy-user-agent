package com.snxy.user.agent.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class SystemUser {
    private Long id;

    private String account;

    private String mobile;

    private String chineseName;

    private String pwd;

    private Date regDate;

    private Byte accountStatus;

    private Date gmtCreate;

    private Date gmtModified;

    private Boolean isDelete;


}