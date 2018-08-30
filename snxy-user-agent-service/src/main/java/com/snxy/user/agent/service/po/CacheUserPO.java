package com.snxy.user.agent.service.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by 24398 on 2018/8/30.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CacheUserPO {

    private Long userId;
    private String chineseName;

    private Integer deviceType ;
    private Long expireTime;

}
