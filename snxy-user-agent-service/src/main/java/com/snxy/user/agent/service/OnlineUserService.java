package com.snxy.user.agent.service;

import com.snxy.user.agent.domain.OnlineUser;

public interface OnlineUserService {
    OnlineUser selectOne(Long systemUserId);
}
