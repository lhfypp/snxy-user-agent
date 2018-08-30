package com.snxy.user.agent.biz;

import com.snxy.user.agent.dao.mapper.SystemUserMapper;
import com.snxy.user.agent.domain.SystemUser;
import com.snxy.user.agent.service.UserVerificationService;
import com.snxy.user.agent.service.vo.SystemUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserVerficationBiz implements UserVerificationService {
    @Autowired
    private SystemUserMapper systemUserMapper;
    @Override
    public SystemUserVO login(String userName, String password) {
        SystemUser record = new SystemUser();
        record.setAccount(userName);
        record.setPwd(password);
        SystemUser systemUser = systemUserMapper.login(record);

        return null;
    }

    @Override
    public SystemUserVO getSystemUserByToken(String token) {

        return null;
    }
}
