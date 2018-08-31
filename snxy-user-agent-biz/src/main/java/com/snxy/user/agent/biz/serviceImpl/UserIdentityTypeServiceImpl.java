package com.snxy.user.agent.biz.serviceImpl;

import com.snxy.user.agent.dao.mapper.UserIdentityTypeMapper;
import com.snxy.user.agent.service.UserIdentityTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by 24398 on 2018/8/31.
 */

@Service
@Slf4j
public class UserIdentityTypeServiceImpl implements UserIdentityTypeService{

    @Resource
    private UserIdentityTypeMapper userIdentityTypeMapper;

}
