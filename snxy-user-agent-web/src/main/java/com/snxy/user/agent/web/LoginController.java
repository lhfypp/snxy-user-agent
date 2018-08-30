package com.snxy.user.agent.web;

import com.snxy.common.util.CheckUtil;
import com.snxy.user.agent.biz.constant.LoginDeviceEnum;
import com.snxy.user.agent.service.UserVerificationService;
import com.snxy.user.agent.service.vo.LoginUserVO;
import com.snxy.user.agent.service.vo.SystemUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

/**
 * Created by 24398 on 2018/8/29.
 */


@Controller
@Slf4j
public class LoginController {

    @Resource
    private UserVerificationService userVerificationService;

    public SystemUserVO login(@RequestBody LoginUserVO loginUserVO){

        loginUserVO.checkParam();
        CheckUtil.isTrue(LoginDeviceEnum.containType(loginUserVO.getDeviceType()),"非法的登陆设备");

        this.userVerificationService.login(loginUserVO);


        return null;
    }

}
