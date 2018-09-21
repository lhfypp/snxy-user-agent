package com.snxy.user.agent.domain;

import lombok.Data;

@Data
public class OnlineUser {
    private Long id;

    private Long systemUserId;

    private String name;

    private String phone;

    private Byte isDelete;

}