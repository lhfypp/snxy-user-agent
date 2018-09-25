package com.snxy.user.agent.biz.serviceImpl;

import com.snxy.common.exception.BizException;
import com.snxy.common.util.AESUtil;
import com.snxy.common.util.CheckUtil;
import com.snxy.common.util.MD5Util;
import com.snxy.common.util.StringUtil;
import com.snxy.user.agent.biz.constant.LoginDeviceEnum;
import com.snxy.user.agent.biz.constant.LoginTypeEnum;
import com.snxy.user.agent.domain.*;
import com.snxy.user.agent.service.*;
import com.snxy.user.agent.service.po.CacheUser;
import com.snxy.user.agent.service.vo.LoginUserVO;
import com.snxy.user.agent.service.vo.SystemUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by 24398 on 2018/8/30.
 */

@Service
@Slf4j
public class UserVerificationServiceImpl implements UserVerificationService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Resource
    private SystemUserService systemUserService;
     @Resource
     private OnlineUserService onlineUserService;
     @Resource
     private StaffService staffService;
     @Resource
     private SystemLoginLogService systemLoginLogService;



    private final String AES_KEY = "snxy-user-agent";
    private final String USER_TOKEN_TEMPLATE = "TOKEN-%s-%s-%s";
    private final String REDIS_CACHE_TEMPLATE = "CACHE-%s-%s";
    private final String LOGIN_SMSCODE_TEMPLATE = "SMSCODE-%s";
    @Value("${token.expire.time}")
    private  String   CACHE_EXPIRE_TIME ;


    private final String DEVICE_PC = "PC";
    private final String DEVICE_MOBILE = "MOBILE";

    private static final Long CACHE_SMSCODE_TIME = 2L;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemUserVO login(LoginUserVO loginUserVO) {

        String username = loginUserVO.getUsername();
        // 根据登陆账号或者手机号查找用户
        if(loginUserVO.getLoginType() == LoginTypeEnum.LOGIN_SMS.getLoginType()){
             // 短信验证码登陆 校验验证码是否正确
             String mobile = loginUserVO.getUsername();
             CheckUtil.isTrue(StringUtil.checkMobile(mobile),"手机号码格式不正确");
             this.checkSmsCode(mobile,loginUserVO.getSmsCode());
        }
        Integer loginType = loginUserVO.getLoginType();
        SystemUser systemUser =  systemUserService.loadSystemUser(username,loginType);
        // 记录登陆日志  --- 默认失败
        log.info("curr   threadId : [{}]  ; threadName : [{}]",Thread.currentThread().getId(),Thread.currentThread().getName());
        HttpServletRequest request = ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest();
        Future<Long>  loginLogFuture = this.systemLoginLogService.writeLoginUserLog(loginUserVO,systemUser,request);

        if(systemUser == null ){
            log.error("登录号 : [{}]  登陆失败 : [{}]",username,"账号或手机号不存在");
            throw new BizException("账号或手机号不存在");
        }else if(systemUser.getAccountStatus() == -1){     // 账号停用
            log.error("登录号 : [{}]  登陆失败 : [{}]",username,"账号停用，请联系新发地管理人员");
            throw new BizException("账号停用，请联系新发地管理人员");
        }else if(systemUser.getAccountStatus() == 1){     // 账号挂失
            log.error("登录号 : [{}]  登陆失败 : [{}]",username,"账号挂失，请联系新发地管理人员");
            throw new BizException("账号挂失，请联系新发地管理人员");
        }

        if(LoginTypeEnum.LOGIN_SMS.getLoginType() != loginType){
            // 不是手机验证码登陆，需要验证密码
            String password = MD5Util.encrypt(loginUserVO.getPassword());
            if( !password.equals(systemUser.getPwd()) ){
                log.error("账号 ：[{}] 登陆失败 ：[{}]",username,"密码错误");
                throw new BizException("密码错误");
            }
        }

        String device = null;
        if(loginUserVO.getDeviceType() == LoginDeviceEnum.PC.getDeviceType()){
            device = DEVICE_PC;
        }else{
            device = DEVICE_MOBILE;
        }
        // token
        Long expireTime = System.currentTimeMillis() + TimeoutUtils.toMillis(Integer.parseInt(CACHE_EXPIRE_TIME),TimeUnit.DAYS);
        String token = this.getToken(systemUser,device,expireTime);
        //存储在redis中

        Map<String,Object> map = this.judgeStaff(systemUser.getId());

        CacheUser cacheUser = this.cacheUser(systemUser,device,loginUserVO.getDeviceType(),map,expireTime,token);
       // 返回 SystemUserVO
       SystemUserVO systemUserVO = SystemUserVO.builder()
                                     .name(systemUser.getChineseName())
                                     .token(token)
                                    // .identityTypes(new ArrayList<>())
                                     .build();

       //  修改登陆状态
        try {
          Long systemLoginLogId = loginLogFuture.get(3,TimeUnit.SECONDS);
          this.systemLoginLogService.setLoginSuccess(systemLoginLogId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return systemUserVO;
    }


/*    @Async
    public Future<Long> writeLoginUserLog(LoginUserVO loginUserVO , SystemUser systemUser, HttpServletRequest request){
        Long systemUserId = systemUser.getId();
        String ip = request.getRemoteAddr();
        SystemLoginLog loginLog = new SystemLoginLog();
          loginLog.setUserId(systemUserId);
          loginLog.setGmtCreate(new Date());
          loginLog.setIp(ip);
          loginLog.setNote("");
          loginLog.setIsSuccess((byte)0);  // 默认没有成功
          loginLog.setIsDelete((byte)1);
          loginLog.setLoginTypeCode(LoginDeviceEnum.getDeviceDesc(loginUserVO.getDeviceType()));

          systemLoginLogService.log(loginLog);
          Long threadId =  Thread.currentThread().getId();
          String threadName = Thread.currentThread().getName();
          log.info("writeLoginUserLog  threadId : [{}]  ; threadName : [{}]",threadId,threadName);
        return new AsyncResult<>(loginLog.getId());
    }


    @Async
    public  void setLoginSuccess(Long systemLoginLogId){
        SystemLoginLog systemLoginLog  = new SystemLoginLog();
           systemLoginLog.setId(systemLoginLogId);
           systemLoginLog.setIsSuccess((byte)1);
        Long threadId =  Thread.currentThread().getId();
        String threadName = Thread.currentThread().getName();
        log.info("setLoginSuccess  threadId : [{}]  ; threadName : [{}]",threadId,threadName);
        this.systemLoginLogService.setLoginSuccess(systemLoginLog);

    }*/



    public void checkSmsCode(String mobile,String smsCode) {
        // 校验手机验证码是否正确
        String redisKey = String.format(LOGIN_SMSCODE_TEMPLATE,mobile);
        String cacheSmsCode = (String) this.redisTemplate.opsForValue().get(redisKey);
        if(cacheSmsCode == null){
            log.error("手机验证码登陆失败 ： [{}]","验证码过期，请重新获取");
            throw new BizException("验证码过期，请重新获取");
        }
        if(!cacheSmsCode.equals(smsCode)){
            log.error("手机验证码登陆失败 ： [{}]","验证码过期，请重新登录");
            throw new BizException("验证码输入有误");
        }
    }

    public String getToken(SystemUser systemUser,String loginDevice,Long expireTime){
        String rowToken = String.format(USER_TOKEN_TEMPLATE,systemUser.getAccount(),loginDevice,expireTime);
        String token = AESUtil.encryptContent(rowToken, AES_KEY);
        return token;
    }

    public Map<String,Object> judgeStaff(Long systemUserId){
        Map<String,Object> map = new HashMap();
        OnlineUser onlineUser = this.onlineUserService.selectOne(systemUserId);
        if(onlineUser != null){
            map.put("userId",onlineUser.getId());
            map.put("beOnlineUser",true);
            return map;
        }
        Staff staff = this.staffService.selectOne(systemUserId);
        if(staff != null){
            map.put("userId",staff.getId());
            map.put("beOnlineUser",false);
            return map;
        }

        throw new BizException("尚未区分系统用户");
    }



    public CacheUser cacheUser(SystemUser systemUser, String loginDevice, Integer deviceType, Map<String,Object> map,Long expireTime, String token){
        String redisKey = String.format(REDIS_CACHE_TEMPLATE,systemUser.getAccount(),loginDevice);
        CacheUser cacheUser = CacheUser.builder()
                                .systemUserId(systemUser.getId())
                                .userId((Long) map.get("userId"))
                                .beOnlineUser((Boolean) map.get("beOnlineUser"))
                                .account(systemUser.getAccount())
                                .token(token)
                                .chineseName(systemUser.getChineseName())
                                .deviceType(deviceType)
                                .expireTime(expireTime)
                                .build();

        redisTemplate.opsForValue().set(redisKey, cacheUser,Integer.parseInt(CACHE_EXPIRE_TIME),TimeUnit.DAYS);

        return cacheUser;
    }

    @Override
    public CacheUser getSystemUserByToken(String token) {
        // 查询redis中token是否存在
        String redisKey = this.getRedisKey(token);
        CacheUser cacheUser = (CacheUser) this.redisTemplate.opsForValue().get(redisKey);
        if(cacheUser == null ){
             throw new BizException("请重新登陆");
        }else if( !token.equals(cacheUser.getToken()) ){
             throw new BizException("其他设备登陆，请重新登陆");
        }

        if(System.currentTimeMillis() > cacheUser.getExpireTime()){
            // 登陆已超时，redis超时机制失败
            this.redisTemplate.delete(redisKey);
        }

        return cacheUser;
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
        String redisKey = String.format(REDIS_CACHE_TEMPLATE,elements[1],elements[2]);

        return redisKey;
    }

    @Override
    public void loginOut(String token) {
         String redisKey = this.getRedisKey(token);
         //清除redis缓存
        this.redisTemplate.delete(redisKey);
    }

    @Override
    public void getSmsCode(String mobile) {
        String smsCode = "111111";
        // 调用
        //保存在redis中，2分钟
        String redisKey = String.format(LOGIN_SMSCODE_TEMPLATE,mobile);
         this.redisTemplate.opsForValue().set(redisKey,smsCode,CACHE_SMSCODE_TIME, TimeUnit.MINUTES);
    }


}
