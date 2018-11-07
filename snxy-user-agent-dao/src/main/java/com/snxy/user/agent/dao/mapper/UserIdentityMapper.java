package com.snxy.user.agent.dao.mapper;

import com.snxy.user.agent.domain.UserIdentity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserIdentityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserIdentity record);

    int insertSelective(UserIdentity record);

    UserIdentity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserIdentity record);

    int updateByPrimaryKey(UserIdentity record);

    List<UserIdentity> getByOnlineUserId(@Param("onlineUserId") Long onlineUserId, @Param("isDelete") Boolean isDelete);
}