package com.snxy.user.agent.service;

import com.snxy.user.agent.domain.SystemUser;

/**
 * Created by 24398 on 2018/8/31.
 */
public interface SystemUserService {
    SystemUser loadSystemUser(String username, Integer loginType);
}
