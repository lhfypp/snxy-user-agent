package com.snxy.user.agent.service.vo;

import com.snxy.user.agent.domain.IdentityType;
import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 * @date 2018-08-29
 */
@Data
public class SystemUserVO {
    private String token;
    //用户信息
    List<IdentityType> identityTypes;

}