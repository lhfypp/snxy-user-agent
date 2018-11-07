package com.snxy.user.agent.service;

import com.snxy.user.agent.domain.IdentityType;

import java.util.List;

/**
 * Created by 24398 on 2018/8/31.
 */

public interface IdentityTypeService {
    List<IdentityType> getByIds(List<Integer> identityIds, Boolean isDelete);
}
