package com.snxy.user.agent.dao.mapper;

import com.snxy.user.agent.domain.OnlineUser;
import org.apache.ibatis.annotations.Param;

public interface OnlineUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OnlineUser record);

    int insertSelective(OnlineUser record);

    OnlineUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OnlineUser record);

    int updateByPrimaryKey(OnlineUser record);

    OnlineUser getBySystemUserId(@Param("systemUserId") Long systemUserId,@Param("isDelete") Boolean isDelete);
}