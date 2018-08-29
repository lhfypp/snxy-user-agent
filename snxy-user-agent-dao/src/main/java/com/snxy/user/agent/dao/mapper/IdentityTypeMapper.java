package com.snxy.user.agent.dao.mapper;

import com.snxy.user.agent.domain.IdentityType;

public interface IdentityTypeMapper {
    int insert(IdentityType record);

    int insertSelective(IdentityType record);
}