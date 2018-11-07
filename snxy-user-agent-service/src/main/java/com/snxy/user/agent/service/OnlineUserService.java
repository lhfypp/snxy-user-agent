package com.snxy.user.agent.service;

import com.snxy.user.agent.domain.OnlineUser;

/**
 * Created by 24398 on 2018/11/5.
 */


public interface OnlineUserService {
    OnlineUser getBySystemUserId(Long systemUserId, Boolean isDelete);

    void insert(OnlineUser onlineUser);
}
