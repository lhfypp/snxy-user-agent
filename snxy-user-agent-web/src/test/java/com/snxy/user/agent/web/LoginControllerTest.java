package com.snxy.user.agent.web;

import com.alibaba.fastjson.JSON;
import com.snxy.user.agent.service.vo.LoginUserVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by 24398 on 2018/8/30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LoginControllerTest {

    @Test
    public void LoginUserVO(){
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setUserName("100002");
        loginUserVO.setPassword("");
        loginUserVO.setDeviceType(1);

        log.info("loginUserVO : [{}]", JSON.toJSONString(loginUserVO));
    }
}
