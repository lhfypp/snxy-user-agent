package com.snxy.user.agent.dao.mapper;

import com.snxy.user.agent.domain.SystemLoginLog;

public interface SystemLoginLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SystemLoginLog record);

    int insertSelective(SystemLoginLog record);

    SystemLoginLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SystemLoginLog record);

    int updateByPrimaryKey(SystemLoginLog record);
}