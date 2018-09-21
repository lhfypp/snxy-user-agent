package com.snxy.user.agent.biz.serviceImpl;

import com.snxy.common.exception.BizException;
import com.snxy.user.agent.biz.constant.LoginTypeEnum;
import com.snxy.user.agent.dao.mapper.SystemUserMapper;
import com.snxy.user.agent.domain.SystemUser;
import com.snxy.user.agent.service.SystemUserService;
import io.netty.handler.codec.compression.Bzip2Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by 24398 on 2018/8/31.
 */

@Service
@Slf4j
public class SystemUserServiceImpl implements SystemUserService {

    @Resource
    private SystemUserMapper systemUserMapper;


    @Override
    public SystemUser loadSystemUser(String username, Integer loginType) {

        SystemUser systemUser = null;

        if(LoginTypeEnum.LOGIN_MOBILE.getLoginType() == loginType|| LoginTypeEnum.LOGIN_SMS.getLoginType() == loginType){
            // 根据手机号码查询
            systemUser = this.systemUserMapper.getByPhoneNumber(username);
        }else if(LoginTypeEnum.LOGIN_ACCOUNT.getLoginType() == loginType){
            // 账号 account 查询
            systemUser =  this.systemUserMapper.getByAccount(username);
        }else{
            // 未知登陆类型
            log.error("登陆失败 ：登陆号 [{}] ,未知登陆类型 ： [{}]" ,username,loginType);
            throw new BizException("未知登陆类型");
        }

        return systemUser;
    }
}
