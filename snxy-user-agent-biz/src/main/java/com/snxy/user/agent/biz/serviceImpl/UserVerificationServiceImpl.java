package com.snxy.user.agent.biz.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.snxy.common.util.AESUtil;
import com.snxy.user.agent.biz.constant.LoginDeviceEnum;
import com.snxy.user.agent.service.UserVerificationService;
import com.snxy.user.agent.service.po.CacheUserPO;
import com.snxy.user.agent.service.vo.LoginUserVO;
import com.snxy.user.agent.service.vo.SystemUserVO;
import com.sun.corba.se.spi.ior.ObjectKey;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.omg.CORBA.TIMEOUT;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by 24398 on 2018/8/30.
 */

@Service
@Slf4j
public class UserVerificationServiceImpl implements UserVerificationService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    private final String AES_KEY = "GDuAdPEZLlCBe683swP9GQ";
    private final String USER_TOKEN_FORMATE = "user-%s-%s";
    private final String REDIS_CACHE_FORMATE = "loginUser-%s-%s";
    private final Long   CACHE_EXPRIRETIME  = 1L;
    private final String DEVICE_PC = "PC";
    private final String DEVICE_MOBILE = "MOBILE";


    @Override
    public SystemUserVO login(LoginUserVO loginUserVO) {
        String loginAccount = loginUserVO.getUserName();

        // 已经验证成功

        // 产生token
        String device = null;
        if(loginUserVO.getDeviceType() == LoginDeviceEnum.PC.getDeviceType()){
            device = DEVICE_PC;
        }else{
            device = DEVICE_MOBILE;
        }
        String rowToken = String.format(USER_TOKEN_FORMATE,1L,device);

        String token = AESUtil.encryptContent(rowToken, Base64.encodeBase64String(Base64.decodeBase64(AES_KEY)));
        //存储在redis中
        String redisKey = String.format(REDIS_CACHE_FORMATE,1L,device);
        CacheUserPO cacheUserPO = CacheUserPO.builder().userId(1L)
                 .chineseName("")
                 .deviceType(loginUserVO.getDeviceType())
                 .expireTime(System.currentTimeMillis() + TimeoutUtils.toMillis(CACHE_EXPRIRETIME,TimeUnit.DAYS))
                 .build();

        redisTemplate.opsForValue().set(redisKey, cacheUserPO,1,TimeUnit.DAYS);



        return null;
    }






    @Override
    public SystemUserVO getSystemUserByToken(String token) {
        return null;
    }
}
