package com.snxy.user.agent.dao;

import com.snxy.user.agent.dao.mapper.IdentityTypeMapper;
import com.snxy.user.agent.domain.IdentityType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class IdentityTypeMapperTests {
    @Resource
    IdentityTypeMapper identityTypeMapper;

    @Test
    public void insert(){
        IdentityType identityType = new IdentityType();
        identityType.setId(10);
        identityType.setIdentityId((byte)1);
        identityType.setIdentityName("司机");
        identityType.setRemark(null);
        int i = identityTypeMapper.insert(identityType);
        Assert.assertTrue("不该为空",i!=0);
        log.info(String.valueOf(i));
    }

    @Test
    public void insertSelective(){
        IdentityType identityType = new IdentityType();
        identityType.setIdentityId((byte)1);
        identityType.setIdentityName("司机");
        identityType.setRemark(null);
        int i = identityTypeMapper.insertSelective(identityType);
        log.info(String.valueOf(i));
    }



}
