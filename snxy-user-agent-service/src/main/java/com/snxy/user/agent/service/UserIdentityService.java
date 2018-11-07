package com.snxy.user.agent.service;

import com.snxy.user.agent.domain.UserIdentity;
import com.snxy.user.agent.service.vo.UserIdentityVO;

import java.util.List;

/**
 * Created by 24398 on 2018/8/31.
 */
public interface UserIdentityService {
    List<UserIdentityVO> getIdentityBySystemUserId(Long SystemUserId);

    void insert(UserIdentity userIdentity);
}
