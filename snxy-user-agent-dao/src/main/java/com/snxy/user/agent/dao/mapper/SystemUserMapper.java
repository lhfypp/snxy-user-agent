package com.snxy.user.agent.dao.mapper;

import com.snxy.user.agent.domain.SystemUser;
import org.apache.ibatis.annotations.Param;

public interface SystemUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SystemUser record);

    int insertSelective(SystemUser record);

    SystemUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SystemUser record);

    int updateByPrimaryKey(SystemUser record);

    SystemUser getByPhoneNumber(@Param("username") String username, @Param("isDelete") Boolean isDelete);

    SystemUser getByAccount(@Param("username") String username, @Param("isDelete") Boolean isDelete);
}