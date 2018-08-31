package com.snxy.user.agent.biz.serviceImplTest;

import com.snxy.user.agent.service.UserVerificationService;
import com.snxy.user.agent.service.vo.LoginUserVO;
import com.snxy.user.agent.service.vo.SystemUserVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Created by 24398 on 2018/8/31.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserVerificationServiceImplTest {

    @Resource
    private UserVerificationService userVerificationService;


    @Test
    public void loginByAccount(){
        LoginUserVO loginUserVO = new LoginUserVO() ;
        loginUserVO.setUsername("100002");
        loginUserVO.setPassword("111111");
        loginUserVO.setLoginType(2);
        loginUserVO.setDeviceType(1);
        log.info(" loginUserVO  -> *** : {}",loginUserVO);

        SystemUserVO systemUserVO = userVerificationService.login(loginUserVO);

        log.info(" systemUserVO  :  [{}]",systemUserVO);
    }


    @Test
    public void loginByPhoneNumber(){
        LoginUserVO loginUserVO = new LoginUserVO() ;
        loginUserVO.setUsername("15001080037");
        loginUserVO.setPassword("111111");
        loginUserVO.setLoginType(1);
        loginUserVO.setDeviceType(5);
        log.info(" loginUserVO  -> *** : {}",loginUserVO);

        SystemUserVO systemUserVO = userVerificationService.login(loginUserVO);

        log.info(" systemUserVO  :  [{}]",systemUserVO);
    }

    @Test
    public void switchIdentityTest(){
        Long systemUserId = 1L;
        Integer identityId = 2;
        userVerificationService.switchIdentity(systemUserId,identityId);
    }


}
