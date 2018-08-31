package com.snxy.user.agent.biz.serviceImpl;

import com.snxy.common.exception.BizException;
import com.snxy.common.util.AESUtil;
import com.snxy.common.util.MD5Util;
import com.snxy.user.agent.biz.constant.LoginDeviceEnum;
import com.snxy.user.agent.dao.mapper.SystemUserMapper;
import com.snxy.user.agent.dao.mapper.UserIdentityTypeMapper;
import com.snxy.user.agent.domain.SystemUser;
import com.snxy.user.agent.domain.UserIdentity;
import com.snxy.user.agent.domain.UserIdentityType;
import com.snxy.user.agent.service.UserVerificationService;
import com.snxy.user.agent.service.po.CacheUserPO;
import com.snxy.user.agent.service.vo.LoginUserVO;
import com.snxy.user.agent.service.vo.SystemUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 24398 on 2018/8/30.
 */

@Service
@Slf4j
public class UserVerificationServiceImpl implements UserVerificationService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private SystemUserMapper systemUserMapper;
    @Resource
    private UserIdentityTypeMapper userIdentityTypeMapper;

    private final String AES_KEY = "snxy-user-agent";
    private final String USER_TOKEN_FORMATE = "TOKEN-%s-%s-%s";
    private final String REDIS_CACHE_FORMATE = "CACHE-%s-%s";
    private final Long   CACHE_EXPRIRETIME  = 1L;

    private final String DEVICE_PC = "PC";
    private final String DEVICE_MOBILE = "MOBILE";


    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemUserVO login(LoginUserVO loginUserVO) {
        // 根据登陆账号查找用户
        String loginAccount = loginUserVO.getUserName();
        SystemUser systemUser =  systemUserMapper.getByAccount(loginAccount);

        if(systemUser == null ){
            log.error("账号 : [{}]  登陆失败 : [{}]",loginAccount,"账号不存在");
            throw new BizException("账号不存在");
        }

        String password = MD5Util.encrypt(loginUserVO.getPassword());
        if( !password.equals(systemUser.getPwd()) ){
            log.error("账号 ：[{}] 登陆失败 ：[{}]",loginAccount,"密码错误");
            throw new BizException("密码错误");
        }
        // 查找角色
        List<UserIdentity> userIdentities = this.userIdentityTypeMapper.listUserIdentityBySystemUserId(systemUser.getId());
        if(userIdentities == null || userIdentities.isEmpty()){
            log.error("账号 ：[{}] 登陆失败 ：[{}]",loginAccount,"没有查询到用户角色");
            throw new BizException("没有查找到用户角色");
        }

        boolean hasActiveIdentity = false;
        for(int i = 0;i< userIdentities.size();i++){
            if(userIdentities.get(i).getIsActive()){
                hasActiveIdentity = true;
            }
        }
       log.info("userIdentities : [{}] ",userIdentities);
        if(!hasActiveIdentity){
            // 没有活跃身份，将第一个身份设置为active
            UserIdentityType userIdentityType = new UserIdentityType();
               userIdentityType.setIsActive(true);
               userIdentityType.setSystemUserId(userIdentities.get(0).getSystemUserId());
               userIdentityType.setIdentityTypeId(userIdentities.get(0).getIdentityId());

            int n = this.userIdentityTypeMapper.updateByPrimaryKey(userIdentityType);
            if( n != 1){
                log.error("账号 ：[{}] 登陆失败 ： [{}]",loginAccount,"设置活跃身份失败");
                throw new BizException("设置活跃身份失败");
            }
            userIdentities.get(0).setIsActive(true);
        }

        String device = null;
        if(loginUserVO.getDeviceType() == LoginDeviceEnum.PC.getDeviceType()){
            device = DEVICE_PC;
        }else{
            device = DEVICE_MOBILE;
        }
        // token
        Long expireTime = System.currentTimeMillis() + TimeoutUtils.toMillis(CACHE_EXPRIRETIME,TimeUnit.DAYS);
        String token = this.getToken(systemUser,device,expireTime);
        //存储在redis中
       CacheUserPO cacheUserPO = this.cacheUser(systemUser,device,loginUserVO.getDeviceType(),expireTime,token);
       // 返回 SystemUserVO
       SystemUserVO systemUserVO = SystemUserVO.builder()
                                     .systemUserId(systemUser.getId())
                                     .name(systemUser.getChineseName())
                                     .token(token)
                                     .identityTypes(userIdentities)
                                     .build();

        return systemUserVO;
    }

    public String getToken(SystemUser systemUser,String loginDevice,Long expireTime){
        String rowToken = String.format(USER_TOKEN_FORMATE,systemUser.getAccount(),loginDevice,expireTime);
        String token = AESUtil.encryptContent(rowToken, AES_KEY);
        return token;
    }

    public CacheUserPO cacheUser(SystemUser systemUser,String loginDevice,Integer deviceType,Long expireTime,String token){
        String redisKey = String.format(REDIS_CACHE_FORMATE,systemUser.getAccount(),loginDevice);
        CacheUserPO cacheUserPO = CacheUserPO.builder().id(systemUser.getId())
                 .account(systemUser.getAccount())
                .token(token)
                .chineseName(systemUser.getChineseName())
                .deviceType(deviceType)
                .expireTime(expireTime)
                .build();

        redisTemplate.opsForValue().set(redisKey, cacheUserPO,CACHE_EXPRIRETIME,TimeUnit.DAYS);

        return cacheUserPO;
    }

    @Override
    public CacheUserPO getSystemUserByToken(String token) {
        // 查询redis中token是否存在
        String redisKey = this.getRedisKey(token);
        CacheUserPO cacheUserPO = (CacheUserPO) this.redisTemplate.opsForValue().get(redisKey);
        if(cacheUserPO == null ){
             throw new BizException("请重新登陆");
        }else if( !token.equals(cacheUserPO.getToken()) ){
             throw new BizException("其他设备登陆，请重新登陆");
        }

        if(System.currentTimeMillis() > cacheUserPO.getExpireTime()){
            // 登陆已超时，redis超时机制失败
            this.redisTemplate.delete(redisKey);
        }

        return cacheUserPO;
    }

    public String getRedisKey(String token){
        String rowToken;
        try {
            // 解密token，获取用户身份信息
            rowToken = AESUtil.decryptContent(token, AES_KEY);
        } catch (Exception e) {
            throw new BizException("解析token失败,请重新登陆");
        }
        // redisKey
        String [] elements = rowToken.split("-");
        String redisKey = String.format(REDIS_CACHE_FORMATE,elements[1],elements[2]);

        return redisKey;
    }

    @Override
    public void loginOut(String token) {
         String redisKey = this.getRedisKey(token);
         //清除redis缓存
        this.redisTemplate.delete(redisKey);
    }
}
