package com.snxy.user.agent.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OnlineUser {
    private Long id;

    private Long systemUserId;

    private String userName;

    private String phone;

    private Byte isDelete;

}