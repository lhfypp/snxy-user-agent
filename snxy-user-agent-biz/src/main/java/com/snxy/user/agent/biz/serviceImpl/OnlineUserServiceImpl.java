package com.snxy.user.agent.biz.serviceImpl;

import com.snxy.user.agent.dao.mapper.OnlineUserMapper;
import com.snxy.user.agent.domain.OnlineUser;
import com.snxy.user.agent.service.OnlineUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class OnlineUserServiceImpl implements OnlineUserService {

    @Resource
    OnlineUserMapper onlineUserMapper;

    @Override
    public OnlineUser selectOne(Long systemUserId) {
        OnlineUser onlineUser = this.onlineUserMapper.selectBySystemUserId(systemUserId);
        return onlineUser;
    }
}
