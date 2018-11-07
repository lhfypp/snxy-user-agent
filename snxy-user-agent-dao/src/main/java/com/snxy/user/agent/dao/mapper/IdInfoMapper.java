package com.snxy.user.agent.dao.mapper;

import com.snxy.user.agent.domain.IdInfo;

public interface IdInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(IdInfo record);

    int insertSelective(IdInfo record);

    IdInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(IdInfo record);

    int updateByPrimaryKey(IdInfo record);
}