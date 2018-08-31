package com.snxy.user.agent.biz.serviceImpl;

import com.snxy.common.exception.BizException;
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

    private final Integer LOGIN_PHONE = 1;
    private final Integer LOGIN_ACCOUNT = 2;

    @Override
    public SystemUser loadSystemUser(String username, Integer loginType) {

        SystemUser systemUser = null;
        if(LOGIN_PHONE == loginType){
            // 手机号登陆
            systemUser = this.systemUserMapper.getByPhoneNumber(username);
        }else if(LOGIN_ACCOUNT == loginType){
            // 账号 account 登陆
           systemUser =  this.systemUserMapper.getByAccount(username);
        }else{
            // 未知登陆类型
            log.error("登陆失败 ：登陆号 [{}] ,未知登陆类型 ： [{}]" ,username,loginType);
            throw new BizException("未知登陆类型");
        }

        return systemUser;
    }
}
