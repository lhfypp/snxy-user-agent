package com.snxy.user.agent.biz.serviceImpl;

import com.snxy.common.exception.BizException;
import com.snxy.common.util.AESUtil;
import com.snxy.common.util.MD5Util;
import com.snxy.user.agent.biz.constant.IdentityTypeEnum;
import com.snxy.user.agent.domain.OnlineUser;
import com.snxy.user.agent.domain.ShortMessage;
import com.snxy.user.agent.domain.SystemUser;
import com.snxy.user.agent.domain.UserIdentity;
import com.snxy.user.agent.service.*;
import com.snxy.user.agent.service.vo.LoginUserVO;
import com.snxy.user.agent.service.vo.SystemUserVO;
import com.snxy.user.agent.service.vo.UserIdentityVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.ognl.IntHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by 24398 on 2018/8/30.
 */

@Service
@Slf4j
public class UserVerificationServiceImpl implements UserVerificationService {

    private final String AES_KEY = "snxy-user-agent";
    private final String USER_TOKEN_MODE = "TOKEN-%s-%s";
    private final String REDIS_CACHE_MODE = "CACHE-%s";  //  CACHE-ACCOUNT
    private final String REDIS_SMSCODE_MODE = "LOGIN-SMSCODE-%s";
    private final String DEVICE_PC = "PC";
    private final String DEVICE_MOBILE = "MOBILE";
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SystemUserService systemUserService;
    @Value("${token.expire.time}")
    private String CACHE_EXPIRE_TIME;
     @Resource
     private ShortMessageService shortMessageService;
     @Resource
     private UserIdentityService  userIdentityService;
     @Resource
     private OnlineUserService onlineUserService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object>  loginOrRegistry(LoginUserVO loginUserVO) {
        // 根据登陆账号查找用户
        String username = loginUserVO.getMobile();
     //   Integer loginType = loginUserVO.getLoginType();
        String device  = this.DEVICE_MOBILE;
        Boolean beRegistry = false;
        SystemUser systemUser = this.systemUserService.loadSystemUser(username);
        if (systemUser == null) {
            // 注册用户
            log.info("登录号 : [{}]  登陆失败 : [{}]", username, "账号或手机号不存在 ,注册该用户");
            beRegistry = true;
            systemUser = this.registry(loginUserVO);
        }

          // 校验码或者密码
         this.checkPassword(loginUserVO.getPassword(),systemUser.getPwd(),loginUserVO.getPwdType(),loginUserVO.getMobile());

         //  存储redis
        SystemUserVO systemUserVO = this.cacheSystemUserVO(systemUser,device);
        Map<String,Object> map = new HashMap();
          map.put("beRegistry",beRegistry);
          map.put("systemUserVO",systemUserVO);
        return map;
    }

    public void checkPassword(String pwd,String dateBasePwd,Integer pwdType,String mobile){
        if(pwdType == 1){
            // 验证码
            String redisKey = String.format(REDIS_SMSCODE_MODE,mobile);
            String smsCode =(String) this.redisTemplate.opsForValue().get(redisKey);
            if(smsCode == null){
                // 过期
                log.error("登陆失败 ： [{}]","验证码过期，请重新获取");
                throw new BizException("验证码过期，请重新获取");
            }else if(!smsCode.equals(pwd)){
                log.error("登陆失败 ： [{}]","验证码输入有误");
                throw new BizException("验证码输入有误");
            }
        }else{
            // 密码
        }
    }




    public SystemUserVO cacheSystemUserVO(SystemUser systemUser, String device) {
        // 查询身份
        List<UserIdentityVO> userIdentityVOS = this.userIdentityService.getIdentityBySystemUserId(systemUser.getId());
        // token
        Long expireTime = System.currentTimeMillis() + TimeoutUtils.toMillis(Integer.parseInt(CACHE_EXPIRE_TIME), TimeUnit.DAYS);
        String token = this.getToken(systemUser, device, expireTime);
        // 返回 SystemUserVO
        SystemUserVO systemUserVO = SystemUserVO.builder()
                .systemUserId(systemUser.getId())
                .name(systemUser.getChineseName())
                .token(token)
                .userIdentityVOS(userIdentityVOS)
                .expireTime(expireTime)
                .mobile(systemUser.getMobile())
                .build();

        String redisKey = String.format(REDIS_CACHE_MODE, systemUser.getAccount());
        redisTemplate.opsForValue().set(redisKey, systemUserVO, Integer.parseInt(CACHE_EXPIRE_TIME), TimeUnit.DAYS);

        return systemUserVO;
    }

    public String getToken(SystemUser systemUser, String loginDevice, Long expireTime) {
        String rowToken = String.format(USER_TOKEN_MODE, systemUser.getAccount(), expireTime);
        String token = AESUtil.encryptContent(rowToken, AES_KEY);
        return token;
    }



    public SystemUser registry(LoginUserVO loginUserVO){
        // 获取账号自增值
        String key = "ACCOUNT_SUFFIX_NUMBER";
        Long number = this.redisTemplate.opsForValue().increment(key,1L);

        //添加SystemUser   账号状态:0,正常;1,停用;2,挂失

        SystemUser systemUser = SystemUser.builder().account("2000000"+ number)
                                                    .accountStatus((byte)0)
                                                    .pwd(MD5Util.encrypt("111111"))
                                                    .gmtCreate(new Date())
                                                    .mobile(loginUserVO.getMobile())
                                                    .regDate(new Date())
                                                    .build();

        this.systemUserService.insert(systemUser);
        // 添加OnlineUser
        OnlineUser onlineUser = OnlineUser.builder().systemUserId(systemUser.getId())
                                                    .phone(loginUserVO.getMobile())
                                                    .build();

        this.onlineUserService.insert(onlineUser);
        // 设置默认身份
        UserIdentity userIdentity = UserIdentity.builder().identityId(IdentityTypeEnum.VISITOR.getId())
                                                 .onlineUserId(onlineUser.getId())
                                                 .build();

        this.userIdentityService.insert(userIdentity);

        return systemUser;
    }


    @Override
    public SystemUserVO getSystemUserByToken(String token) {
        // 查询redis中token是否存在
        String redisKey = this.getRedisKey(token);
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
        String redisKey = String.format(REDIS_CACHE_MODE, elements[1]);

        return redisKey;
    }



    @Override
    public void loginOut(String token) {
        String redisKey = this.getRedisKey(token);
        //清除redis缓存
        this.redisTemplate.delete(redisKey);
    }


    @Override
    public String getSmsCode(String mobile) {
        String smsCode = "123456";
        log.info("获取手机验证码 ，mobile : [{}] ,验证码 ： [{}]",mobile,smsCode);
        // 保存在redis中
         String redisKey = String.format(REDIS_SMSCODE_MODE,mobile);
         this.redisTemplate.opsForValue().set(redisKey,smsCode,30,TimeUnit.SECONDS);
        // 记录在数据库中
        ShortMessage shortMessage = ShortMessage.builder()
                                          .content(smsCode)
                                          .gmtCreate(new Date())
                                          .receiverMobile(mobile)
                                          .sendTime(new Date())
                                          .build();

        this.shortMessageService.saveShortMessage(shortMessage);
        return smsCode;
    }


    @Override
    public SystemUserVO changCacheUser(Long systemUserId) {
        // 查找SystemUser
        SystemUser systemUser = this.systemUserService.getById(systemUserId,false);
        //
        String redisKey = String.format(REDIS_CACHE_MODE,systemUser.getAccount());
        SystemUserVO oldSystemUserVO=(SystemUserVO) this.redisTemplate.opsForValue().get(redisKey);
        if (oldSystemUserVO == null) {
            log.error("刷新redis缓存用户对象失败 : [{}]","缓存用户已过期，请重新登陆");
            throw new BizException("请重新登陆");
        }

        if (System.currentTimeMillis() > oldSystemUserVO.getExpireTime()) {
            // 登陆已超时，redis超时机制失败
            this.redisTemplate.delete(redisKey);
            log.error("刷新redis缓存用户对象失败 : [{}]","缓存用户过期未自动删除");
            return oldSystemUserVO;
        }
        // 查询身份
        List<UserIdentityVO> userIdentityVOS = this.userIdentityService.getIdentityBySystemUserId(systemUser.getId());

        SystemUserVO systemUserVO = SystemUserVO.builder().mobile(systemUser.getMobile())
                                                          .systemUserId(systemUserId)
                                                          .token(oldSystemUserVO.getToken())
                                                          .name(systemUser.getChineseName())
                                                          .userIdentityVOS(userIdentityVOS)
                                                          .expireTime(oldSystemUserVO.getExpireTime())
                                                          .build();


        this.redisTemplate.opsForValue().set(redisKey,systemUserVO);

        return systemUserVO;
    }




}
