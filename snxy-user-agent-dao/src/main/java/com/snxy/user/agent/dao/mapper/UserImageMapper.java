package com.snxy.user.agent.dao.mapper;

import com.snxy.user.agent.domain.UserImage;

public interface UserImageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserImage record);

    int insertSelective(UserImage record);

    UserImage selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserImage record);

    int updateByPrimaryKey(UserImage record);
}