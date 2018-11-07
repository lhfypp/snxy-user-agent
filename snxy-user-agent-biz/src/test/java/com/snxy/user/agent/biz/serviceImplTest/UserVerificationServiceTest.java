package com.snxy.user.agent.biz.serviceImplTest;

import com.snxy.user.agent.service.UserVerificationService;
import com.snxy.user.agent.service.vo.SystemUserVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by 24398 on 2018/11/6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserVerificationServiceTest {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private UserVerificationService userVerificationService;

    @Test
    public void redisIncrementTest(){
        String key = "test";
        Long number = redisTemplate.opsForValue().increment(key,1L);

        log.info("number :------->   [{}]",number);
    }


    @Test
            public void redisIncrementTest2(){
        String key = "test";
        redisTemplate.opsForValue().set(key,"123",120, TimeUnit.SECONDS);


    }

    @Test
    public void redisIncrementTest3(){
        String key = "test";
        Long number =  redisTemplate.getExpire(key,TimeUnit.SECONDS);
        log.info("number  ---------》 [{}]",number);

    }



    @Test
    public void redisIncrementTest4(){
        String key = "test1";
        Long number =  redisTemplate.getExpire(key,TimeUnit.SECONDS);
        String value = (String) redisTemplate.opsForValue().get(key);
        log.info("number  ---------》 [{}]",number);
        log.info("value ---------> [{}]",value);

    }

    @Test
    public void getSmsCodeTest(){
       String smsCode =  userVerificationService.getSmsCode("15001080024");
       log.info("smsCode  : [{}]",smsCode);

    }


    @Test
    public void changCacheUserTest(){
        SystemUserVO systemUserVO =this.userVerificationService.changCacheUser(13L);
        log.info("systemUserVO ; [{}]",systemUserVO);
    }




}
