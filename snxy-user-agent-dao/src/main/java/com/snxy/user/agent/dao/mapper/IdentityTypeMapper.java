package com.snxy.user.agent.dao.mapper;


import com.snxy.user.agent.domain.IdentityType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IdentityTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IdentityType record);

    int insertSelective(IdentityType record);

    IdentityType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IdentityType record);

    int updateByPrimaryKey(IdentityType record);

    List<IdentityType> selectAllType();

    List<IdentityType> selectByIds(@Param("list") List<Integer> identityIds,@Param("isDelete") Boolean isDelete);
}