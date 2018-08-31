package com.snxy.user.agent.dao;

import com.snxy.user.agent.dao.mapper.UserIdentityTypeMapper;
import com.snxy.user.agent.domain.UserIdentity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserIdentityTypeTests {
    @Resource
    UserIdentityTypeMapper userIdentityTypeMapper;

    @Test
    public void listUserIdentityBySystemUserId(){
        List<UserIdentity> list1 = userIdentityTypeMapper.listUserIdentityBySystemUserId((long)1);
        List<UserIdentity> list2 = userIdentityTypeMapper.listUserIdentityBySystemUserId((long)10);
        log.info("list1:[{}]",list1);
        log.info("list2:[{}]",list2);
    }
}
