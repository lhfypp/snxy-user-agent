package com.snxy.user.agent.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
//查询用户身份的映射表
public class UserIdentity {
    private Long systemUserId;
    private Integer identityId;
    private String identityName;
}
