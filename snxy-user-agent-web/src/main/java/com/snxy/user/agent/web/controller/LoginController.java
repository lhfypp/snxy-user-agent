package com.snxy.user.agent.web.controller;

import com.snxy.common.exception.BizException;
import com.snxy.common.response.ResultData;
import com.snxy.common.util.CheckUtil;
import com.snxy.common.util.StringUtil;
import com.snxy.user.agent.biz.constant.LoginDeviceEnum;
import com.snxy.user.agent.service.UserVerificationService;
import com.snxy.user.agent.service.vo.LoginUserVO;
import com.snxy.user.agent.service.vo.SystemUserVO;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by 24398 on 2018/8/29.
 */


@RestController
@Slf4j
@RequestMapping("/user")
public class LoginController {

    @Resource
    private UserVerificationService userVerificationService;


    /***
     * 获取手机号
     * @param mobile
     */
    @RequestMapping("/smsCode")
    public ResultData getSmsCode(String mobile){
        if(!StringUtil.checkMobile(mobile)){
            log.error("获取登陆验证码出错 ： [{}]","手机号有误，请输入正确手机号");
            throw new BizException("手机号有误，请输入正确手机号");
        };

         this.userVerificationService.getSmsCode(mobile);
        return ResultData.success(null);
    }


    /**
     * 登陆
     * @param loginUserVO
     * @return
     */
    @RequestMapping("/login")
    public ResultData<SystemUserVO> login(LoginUserVO loginUserVO){

        loginUserVO.checkParam();
        CheckUtil.isTrue(LoginDeviceEnum.containType(loginUserVO.getDeviceType()),"非法的登陆设备");

        Map<String,Object> map = this.userVerificationService.loginOrRegistry(loginUserVO);
         if((Boolean) map.get("beRegistry")){
             return ResultData.success(1001,null,(SystemUserVO) map.get("systemUserVO")) ;
         }
       return ResultData.success((SystemUserVO) map.get("systemUserVO"));
    }


    /***
     * 校验token
     * @param token
     * @return
     */
    @RequestMapping("/checkToken")
    public ResultData<SystemUserVO> checkToken(@RequestParam(value = "token",required = true) String token){
        CheckUtil.isTrue(StringUtil.isNotBlank(token),"token不能为空");
        SystemUserVO systemUserVO = this.userVerificationService.getSystemUserByToken(token);
        return ResultData.success(systemUserVO);
    }


    /**
     * 退出登陆
     * @param token
     * @return
     */
    @RequestMapping("/loginOut")
    public ResultData loginOut(@RequestParam(value = "token",required = true)String token){
        CheckUtil.isTrue(StringUtil.isNotBlank(token),"token不能为空");
        this.userVerificationService.loginOut(token);
        return ResultData.success("");
    }


    @RequestMapping("/change/CacheUser")
    public void getSystemUser(Long systemUserId){
        // 返回SystemUser
        if(systemUserId == null || systemUserId <= 0){
            return;
        }
        SystemUserVO systemUserVO = this.userVerificationService.changCacheUser(systemUserId);
        log.info("systemUserVO  : [{}]",systemUserVO);
        return ;
    }





}
