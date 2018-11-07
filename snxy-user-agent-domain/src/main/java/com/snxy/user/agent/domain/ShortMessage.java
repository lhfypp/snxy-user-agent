package com.snxy.user.agent.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class ShortMessage {
    private Long id;

    private String content;

    private String remark;

    private String receiverMobile;

    private Date sendTime;

    private Date gmtCreate;

    private Boolean isDelete;

    private Date gmtModified;
}