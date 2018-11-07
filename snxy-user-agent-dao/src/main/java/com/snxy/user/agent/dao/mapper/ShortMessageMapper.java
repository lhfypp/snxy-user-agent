package com.snxy.user.agent.dao.mapper;

import com.snxy.user.agent.domain.ShortMessage;

public interface ShortMessageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShortMessage record);

    int insertSelective(ShortMessage record);

    ShortMessage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShortMessage record);

    int updateByPrimaryKey(ShortMessage record);
}