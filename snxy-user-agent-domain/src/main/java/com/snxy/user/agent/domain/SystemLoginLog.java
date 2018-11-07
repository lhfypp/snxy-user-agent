package com.snxy.user.agent.domain;

import lombok.Data;

import java.util.Date;
@Data
public class SystemLoginLog {
    private Long id;

    private Long userId;

    private String loginTypeCode;

    private Byte isSuccess;

    private String ip;

    private String note;

    private Date gmtCreate;

    private Byte isDelete;

}