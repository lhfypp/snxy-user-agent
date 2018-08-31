package com.snxy.user.agent.dao;

import com.snxy.user.agent.dao.mapper.UserIdentityTypeMapper;
import com.snxy.user.agent.domain.UserIdentity;
import com.snxy.user.agent.domain.UserIdentityType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 24398 on 2018/8/31.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserIdentityTypeMapperTest {

    @Resource
    private UserIdentityTypeMapper userIdentityTypeMapper;

     @Test
    public void updateByPrimaryKey(){
         UserIdentityType userIdentityType = new UserIdentityType();
           userIdentityType.setIdentityTypeId(1);
           userIdentityType.setSystemUserId(3L);
           userIdentityType.setIsActive(false);
         int n =   this.userIdentityTypeMapper.updateByPrimaryKey(userIdentityType);

        if(n != 1){
            log.info(" updateByPrimaryKey 影响行数: [{}]",n);
        }else{
            log.info(" updateByPrimaryKey 影响行数: [{}]",n);
        }
    }

    @Test
    public void listUserIdentityBySystemUserId(){
        List<UserIdentity> userIdentities =  this.userIdentityTypeMapper.listUserIdentityBySystemUserId(2L);
        log.info(" userIdentities : [{}] ",userIdentities);

    }

}
