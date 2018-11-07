package com.snxy.user.agent.biz.serviceImpl;

import com.snxy.common.exception.BizException;
import com.snxy.user.agent.dao.mapper.SystemUserMapper;
import com.snxy.user.agent.domain.SystemUser;
import com.snxy.user.agent.service.SystemUserService;
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
    public SystemUser loadSystemUser(String username) {

        SystemUser systemUser = null;
        systemUser = this.systemUserMapper.getByPhoneNumber(username,false);
        return systemUser;
    }

    @Override
    public void insert(SystemUser systemUser) {
        this.systemUserMapper.insertSelective(systemUser);
    }


    @Override
    public SystemUser getById(Long systemUserId, Boolean isDelete) {
        SystemUser systemUser = this.systemUserMapper.selectByPrimaryKey(systemUserId);
        if(systemUser == null || systemUser.getIsDelete()){
            return null;
        }
        return systemUser;
    }
}
