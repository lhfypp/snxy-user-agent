package com.snxy.user.agent.service.vo;

import lombok.Builder;
import lombok.Data;

/**
 * Created by 24398 on 2018/11/5.
 */
@Data
@Builder
public class UserIdentityVO {
    private Integer identityTypeId;
    private String identityName;


}
