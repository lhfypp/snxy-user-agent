package com.snxy.user.agent.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserIdentity {
    private Long id;

    private Long onlineUserId;

    private Integer identityId;

    private Boolean isDelete;


}