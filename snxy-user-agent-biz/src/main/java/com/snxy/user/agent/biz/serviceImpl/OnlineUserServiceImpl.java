package com.snxy.user.agent.biz.serviceImpl;

import com.snxy.user.agent.dao.mapper.OnlineUserMapper;
import com.snxy.user.agent.domain.OnlineUser;
import com.snxy.user.agent.service.OnlineUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by 24398 on 2018/11/5.
 */
@Service
@Slf4j
public class OnlineUserServiceImpl implements OnlineUserService {

    @Resource
    private OnlineUserMapper onlineUserMapper;

    @Override
    public OnlineUser getBySystemUserId(Long systemUserId, Boolean isDelete) {
        OnlineUser onlineUser = this.onlineUserMapper.getBySystemUserId(systemUserId,isDelete);
        return onlineUser;
    }

    @Override
    public void insert(OnlineUser onlineUser) {
        this.onlineUserMapper.insertSelective(onlineUser);
    }
}
