package com.snxy.user.agent.biz.serviceImpl;

import com.snxy.common.exception.BizException;
import com.snxy.common.util.AESUtil;
import com.snxy.common.util.MD5Util;
import com.snxy.user.agent.biz.constant.LoginDeviceEnum;
import com.snxy.user.agent.dao.mapper.UserIdentityTypeMapper;
import com.snxy.user.agent.domain.SystemUser;
import com.snxy.user.agent.domain.UserIdentity;
import com.snxy.user.agent.domain.UserIdentityType;
import com.snxy.user.agent.service.SystemUserService;
import com.snxy.user.agent.service.UserVerificationService;
import com.snxy.user.agent.service.po.CacheUser;
import com.snxy.user.agent.service.vo.LoginUserVO;
import com.snxy.user.agent.service.vo.SystemUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    private final String AES_KEY = "snxy-user-agent";
    private final String USER_TOKEN_FORMATE = "TOKEN-%s-%s-%s";
    private final String REDIS_CACHE_FORMATE = "CACHE-%s-%s";
    private final String DEVICE_PC = "PC";
    private final String DEVICE_MOBILE = "MOBILE";
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SystemUserService systemUserService;
    @Resource
    private UserIdentityTypeMapper userIdentityTypeMapper;
    @Value("${token.expire.time}")
    private String CACHE_EXPIRE_TIME;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemUserVO login(LoginUserVO loginUserVO) {
        // 根据登陆账号查找用户
        String username = loginUserVO.getUsername();
        Integer loginType = loginUserVO.getLoginType();
        SystemUser systemUser = systemUserService.loadSystemUser(username, loginType);
        if (systemUser == null) {
            log.error("登录号 : [{}]  登陆失败 : [{}]", username, "账号或手机号不存在");
            throw new BizException("账号或手机号不存在");
        }

        String password = MD5Util.encrypt(loginUserVO.getPassword());
        if (!password.equals(systemUser.getPwd())) {
            log.error("账号 ：[{}] 登陆失败 ：[{}]", username, "密码错误");
            throw new BizException("密码错误");
        }
        // 查找角色
        List<UserIdentity> userIdentities = this.userIdentityTypeMapper.listUserIdentityBySystemUserId(systemUser.getId());
        if (userIdentities == null || userIdentities.isEmpty()) {
            log.error("账号 ：[{}] 登陆失败 ：[{}]", username, "没有查询到用户角色");
            throw new BizException("没有查找到用户角色");
        }

        boolean hasActiveIdentity = false;
        for (int i = 0; i < userIdentities.size(); i++) {
            if (userIdentities.get(i).getIsActive()) {
                hasActiveIdentity = true;
            }
        }
        log.info("userIdentities : [{}] ", userIdentities);
        if (!hasActiveIdentity) {
            // 没有活跃身份，将第一个身份设置为active
            UserIdentityType userIdentityType = new UserIdentityType();
            userIdentityType.setIsActive(true);
            userIdentityType.setSystemUserId(userIdentities.get(0).getSystemUserId());
            userIdentityType.setIdentityTypeId(userIdentities.get(0).getIdentityId());

            int n = this.userIdentityTypeMapper.updateByPrimaryKey(userIdentityType);
            if (n != 1) {
                log.error("账号 ：[{}] 登陆失败 ： [{}]", username, "设置活跃身份失败");
                throw new BizException("设置活跃身份失败");
            }
            userIdentities.get(0).setIsActive(true);
        }

        String device = null;
        if (loginUserVO.getDeviceType() == LoginDeviceEnum.PC.getDeviceType()) {
            device = DEVICE_PC;
        } else {
            device = DEVICE_MOBILE;
        }
        // token
        Long expireTime = System.currentTimeMillis() + TimeoutUtils.toMillis(Integer.parseInt(CACHE_EXPIRE_TIME), TimeUnit.DAYS);
        String token = this.getToken(systemUser, device, expireTime);
        //存储在redis中
//        CacheUser cacheUser = this.cacheUser(systemUser, device, loginUserVO.getDeviceType(), expireTime, token);
        // 返回 SystemUserVO
        SystemUserVO systemUserVO = SystemUserVO.builder()
                .systemUserId(systemUser.getId())
                .name(systemUser.getChineseName())
                .token(token)
                .identityTypes(userIdentities)
                .expireTime(expireTime)
                .account(systemUser.getAccount())
                .build();
        //存储在redis中
        cacheUser(systemUserVO,device);
        return systemUserVO;
    }

    public String getToken(SystemUser systemUser, String loginDevice, Long expireTime) {
        String rowToken = String.format(USER_TOKEN_FORMATE, systemUser.getAccount(), loginDevice, expireTime);
        String token = AESUtil.encryptContent(rowToken, AES_KEY);
        return token;
    }

    public CacheUser cacheUser(SystemUser systemUser, String loginDevice, Integer deviceType, Long expireTime, String token) {
        String redisKey = String.format(REDIS_CACHE_FORMATE, systemUser.getAccount(), loginDevice);
        CacheUser cacheUser = CacheUser.builder().id(systemUser.getId())
                .account(systemUser.getAccount())
                .token(token)
                .chineseName(systemUser.getChineseName())
                .deviceType(deviceType)
                .expireTime(expireTime)
                .account(systemUser.getAccount())
                .build();

        redisTemplate.opsForValue().set(redisKey, cacheUser, Integer.parseInt(CACHE_EXPIRE_TIME), TimeUnit.DAYS);

        return cacheUser;
    }

    public  void cacheUser(SystemUserVO systemUserVO,String loginDevice){
        String redisKey = String.format(REDIS_CACHE_FORMATE, systemUserVO.getAccount(), loginDevice);

        redisTemplate.opsForValue().set(redisKey, systemUserVO, Integer.parseInt(CACHE_EXPIRE_TIME), TimeUnit.DAYS);
    }

    @Override
    public SystemUserVO getSystemUserByToken(String token) {
        // 查询redis中token是否存在
        String redisKey = this.getRedisKey(token);
//        CacheUser cacheUser = (CacheUser) this.redisTemplate.opsForValue().get(redisKey);
//        if (cacheUser == null) {
//            throw new BizException("请重新登陆");
//        } else if (!token.equals(cacheUser.getToken())) {
//            throw new BizException("其他设备登陆，请重新登陆");
//        }

//        if (System.currentTimeMillis() > cacheUser.getExpireTime()) {
//            // 登陆已超时，redis超时机制失败
//            this.redisTemplate.delete(redisKey);
//        }
        SystemUserVO systemUserVO=(SystemUserVO) this.redisTemplate.opsForValue().get(redisKey);
        if (systemUserVO == null) {
            throw new BizException("请重新登陆");
        } else if (!token.equals(systemUserVO.getToken())) {
            throw new BizException("其他设备登陆，请重新登陆");
        }

        if (System.currentTimeMillis() > systemUserVO.getExpireTime()) {
            // 登陆已超时，redis超时机制失败
            this.redisTemplate.delete(redisKey);
        }

        return systemUserVO;
    }

    public String getRedisKey(String token) {
        String rowToken;
        try {
            // 解密token，获取用户身份信息
            rowToken = AESUtil.decryptContent(token, AES_KEY);
        } catch (Exception e) {
            throw new BizException("解析token失败,请重新登陆");
        }
        // redisKey
        String[] elements = rowToken.split("-");
        String redisKey = String.format(REDIS_CACHE_FORMATE, elements[1], elements[2]);

        return redisKey;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void switchIdentity(Long systemUserId, Integer identityId) {

        // 查看当前对应的是否为活跃状态
        UserIdentityType identityTypeParam = new UserIdentityType();
        identityTypeParam.setSystemUserId(systemUserId);
        identityTypeParam.setIdentityTypeId(identityId);

        UserIdentityType userIdentityType = this.userIdentityTypeMapper.selectByPrimaryKey(identityTypeParam);
        if (userIdentityType == null) {
            log.error("切换身份失败 ： [{}]", "没有查询到对应身份");
            throw new BizException("没有查询到对应身份");
        }

        if (!userIdentityType.getIsActive()) {
            // 将原来的身份设为非活跃状态
            List<UserIdentity> userIdentities = this.userIdentityTypeMapper.listUserIdentityBySystemUserId(systemUserId);
            userIdentities.forEach(userIdentity -> {
                if (userIdentity.getIsActive()) {
                    UserIdentityType inactiveUserIdentityType = new UserIdentityType();
                    inactiveUserIdentityType.setSystemUserId(userIdentity.getSystemUserId());
                    inactiveUserIdentityType.setIdentityTypeId(userIdentity.getIdentityId());
                    inactiveUserIdentityType.setIsActive(false);
                    this.userIdentityTypeMapper.updateByPrimaryKey(inactiveUserIdentityType);
                }
            });
            // 切换身份
            identityTypeParam.setIsActive(true);
            this.userIdentityTypeMapper.updateByPrimaryKey(identityTypeParam);

        }


    }

    @Override
    public void loginOut(String token) {
        String redisKey = this.getRedisKey(token);
        //清除redis缓存
        this.redisTemplate.delete(redisKey);
    }
}
