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
public class CacheUser {

    private Long  id;
    private String account;
    private String chineseName;
    private String token;
    private Integer deviceType ;
    private Long expireTime;

}
