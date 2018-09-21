package com.snxy.user.agent.dao.mapper;


import com.snxy.user.agent.domain.Staff;
import org.apache.ibatis.annotations.Param;

public interface StaffMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Staff record);

    int insertSelective(Staff record);

    Staff selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Staff record);

    int updateByPrimaryKey(Staff record);

    Staff selectBySystemUserId(@Param("systemUserId") Long systemUserId);
}