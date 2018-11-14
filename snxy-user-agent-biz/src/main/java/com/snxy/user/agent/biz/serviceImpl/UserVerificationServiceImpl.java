package com.snxy.user.agent.biz.serviceImpl;

import com.snxy.common.exception.BizException;
import com.snxy.common.response.ResultData;
import com.snxy.common.util.AESUtil;
import com.snxy.common.util.MD5Util;
import com.snxy.user.agent.biz.constant.IdentityTypeEnum;
import com.snxy.user.agent.biz.fegin.SmsService;
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
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
     @Resource
     private SmsService smsService;


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
            if(loginUserVO.getPwdType() == 1 ){
                // 校验码
                this.checkSmsCode(loginUserVO.getPassword(),loginUserVO.getMobile());
                // 验证码登陆    注册用户
                log.info("登录号 : [{}]  登陆失败 : [{}]", username, "账号或手机号不存在 ,注册该用户");
                beRegistry = true;
                systemUser = this.registry(loginUserVO);
            }else if(loginUserVO.getPwdType() == 2){
                // 密码登陆
                log.error("登陆号 ：[{}] 登陆失败 ：[{}]",username,"没有查询到用户");
                throw new BizException("没有查询到用户");
            }else{
                log.error("登陆号 ：[{}] 登陆失败 ：[{}]",username,"未知的登陆密码类型");
                throw new BizException("未知的登陆密码类型");
            }
        }

          // 校验密码
        if(loginUserVO.getPwdType() == 2){
            // 密码
            if(!systemUser.getPwd().equals(MD5Util.encrypt(loginUserVO.getPassword()))){
                log.error("登陆失败 ： [{}]","密码有误");
                throw new BizException("密码有误");
            }
        }

         //  存储redis
        SystemUserVO systemUserVO = this.cacheSystemUserVO(systemUser,device);
        Map<String,Object> map = new HashMap();
          map.put("beRegistry",beRegistry);
          map.put("systemUserVO",systemUserVO);
        log.info("systemUserVO : ----->  [{}]",systemUserVO);
        return map;
    }

    public void checkSmsCode(String smsCode,String mobile){
        // 验证码
        String redisKey = String.format(REDIS_SMSCODE_MODE,mobile);
        String redisSmsCode =(String) this.redisTemplate.opsForValue().get(redisKey);
        if(smsCode == null){
            // 过期
            log.error("登陆失败 ： [{}]","验证码过期，请重新获取");
            throw new BizException("验证码过期，请重新获取");
        }else if(!redisSmsCode.equals(smsCode)){
            log.error("登陆失败 ： [{}]","验证码输入有误");
            throw new BizException("验证码输入有误");
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
        UserIdentity userIdentity = UserIdentity.builder()
                                                 .identityId(IdentityTypeEnum.VISITOR.getId())
                                                 .onlineUserId(onlineUser.getId())
                                                 .build();

        this.userIdentityService.insert(userIdentity);

        return systemUser;
    }


    @Override
    public SystemUserVO getSystemUserByToken(String token) {
        // 查询redis中token是否存在
        String redisKey = this.getRedisKey(token);
        SystemUserVO systemUserVO = (SystemUserVO) this.redisTemplate.opsForValue().get(redisKey);
        if (systemUserVO == null) {
            log.error("校验token失败 ：[{}]","请重新登陆");
            throw new BizException("请重新登陆");
        } else if (!token.equals(systemUserVO.getToken())) {
            log.error("校验token失败 ：[{}]","其他设备登陆，请重新登陆");
            throw new BizException("其他设备登陆，请重新登陆");
        }

        if (System.currentTimeMillis() > systemUserVO.getExpireTime()) {
            // 登陆已超时，redis超时机制失败
            log.error("校验token失败 ：[{}]","redis超时机制失效");
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
        // 生成验证码
        String smsCode = this.getSmsCode();
        // 调用smsService
        ResultData resultData =  this.smsService.sendSmsCode(mobile,smsCode,1L);
        if(!resultData.isResult()){
            log.error("手机号码 ： [{}] ，发送验证码失败",mobile);
            throw new BizException("发送验证码失败");
        }
        log.info("获取手机验证码 ，mobile : [{}] ,验证码 ： [{}]",mobile,smsCode);
        // 保存在redis中
         String redisKey = String.format(REDIS_SMSCODE_MODE,mobile);
         this.redisTemplate.opsForValue().set(redisKey,smsCode,20,TimeUnit.MINUTES);
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


    public String getSmsCode(){
        StringBuilder sb = new StringBuilder("");
        for(int i =0;i< 6;i++){
           Integer code =(int)(Math.random()*10);
            sb.append(code);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("");
        for(int i =0;i< 6;i++){
            Integer code =(int)(Math.random()*10);
            sb.append(code);
        }
        System.out.println( "sb  :  ----> "+sb.toString() +":  length: "+sb.toString().length());
    }


    @Override
    public SystemUserVO changCacheUser(Long systemUserId) {
        // 查找SystemUser
        SystemUser systemUser = this.systemUserService.getById(systemUserId,false);
        //
        String redisKey = String.format(REDIS_CACHE_MODE,systemUser.getAccount());
        SystemUserVO oldSystemUserVO = (SystemUserVO) this.redisTemplate.opsForValue().get(redisKey);
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
