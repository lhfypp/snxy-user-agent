package com.snxy.user.agent.web.controller;

import com.snxy.common.response.ResultData;
import com.snxy.common.util.CheckUtil;
import com.snxy.common.util.StringUtil;
import com.snxy.user.agent.biz.constant.LoginDeviceEnum;
import com.snxy.user.agent.service.UserVerificationService;
import com.snxy.user.agent.service.po.CacheUser;
import com.snxy.user.agent.service.vo.LoginUserVO;
import com.snxy.user.agent.service.vo.SystemUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by 24398 on 2018/8/29.
 */


@RestController
@Slf4j
@RequestMapping("/user")
public class LoginController {

    @Resource
    private UserVerificationService userVerificationService;

    /**
     * 登陆
     * @param loginUserVO
     * @return
     */
    @RequestMapping("/login")
    public ResultData<SystemUserVO> login(LoginUserVO loginUserVO){

        loginUserVO.checkParam();
        CheckUtil.isTrue(LoginDeviceEnum.containType(loginUserVO.getDeviceType()),"非法的登陆设备");

        SystemUserVO systemUserVO = this.userVerificationService.login(loginUserVO);

       return ResultData.success(systemUserVO);
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

    /***
     * 切换身份
     * @param identityId
     * @return
     */
    @RequestMapping("/switchIdentity")
    public ResultData  switchIdentity(String token,Integer identityId){
        CheckUtil.isTrue(StringUtil.isNotBlank(token),"token不能为空");
        Long systemUserId = 1L;
        this.userVerificationService.switchIdentity(systemUserId,identityId);
        return ResultData.success("");
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





}
