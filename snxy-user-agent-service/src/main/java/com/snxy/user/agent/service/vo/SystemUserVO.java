package com.snxy.user.agent.service.vo;

import com.snxy.user.agent.domain.UserIdentity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Administrator
 * @date 2018-08-29
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemUserVO {
    private String token;
    //用户信息
    List<UserIdentity> identityTypes;

    private String name;
    private Long systemUserId;

}