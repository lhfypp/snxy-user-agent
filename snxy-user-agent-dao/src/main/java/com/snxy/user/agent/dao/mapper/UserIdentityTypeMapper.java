package com.snxy.user.agent.dao.mapper;

import com.snxy.user.agent.domain.UserIdentity;
import com.snxy.user.agent.domain.UserIdentityType;
import com.snxy.user.agent.domain.UserIdentityTypeKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserIdentityTypeMapper {
//    int deleteByPrimaryKey(UserIdentityTypeKey key);

    int insert(UserIdentityType record);

    int insertSelective(UserIdentityType record);

    UserIdentityType selectByPrimaryKey(UserIdentityTypeKey key);

    int updateByPrimaryKeySelective(UserIdentityType record);

    int updateByPrimaryKey(UserIdentityType record);

    List<UserIdentity> listUserIdentityBySystemUserId(@Param("systemUserId") Long systemUserId);

}