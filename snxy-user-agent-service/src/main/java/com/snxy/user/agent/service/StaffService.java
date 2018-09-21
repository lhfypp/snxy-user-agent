package com.snxy.user.agent.service;

import com.snxy.user.agent.domain.Staff;

public interface StaffService {
    Staff selectOne(Long systemUserId);
}
