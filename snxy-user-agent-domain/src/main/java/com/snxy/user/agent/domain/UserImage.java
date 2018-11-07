package com.snxy.user.agent.domain;

import lombok.Data;

import java.util.Date;
@Data
public class UserImage {
    private Long id;

    private Long systemUserId;

    private String fileType;

    private Integer fileSize;

    private String fileName;

    private String url;

    private String property1;

    private String property2;

    private Date gmtCreate;

    private Date gmtModified;

    private Byte isDelete;


}