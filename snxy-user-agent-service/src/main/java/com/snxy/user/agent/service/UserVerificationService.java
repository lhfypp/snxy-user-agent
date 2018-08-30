package com.snxy.user.agent.service;

import com.snxy.user.agent.service.po.CacheUserPO;
import com.snxy.user.agent.service.vo.LoginUserVO;
import com.snxy.user.agent.service.vo.SystemUserVO;

/**
 * @author Administrator
 * @date 2018-08-29
 */
public interface UserVerificationService {
    /**
     * 用户登陆
     * @param loginUserVO
     * @return
     */
    SystemUserVO login(LoginUserVO loginUserVO);

    /**
     * 由token获取用户信息
     * @param token
     * @return
     */
    CacheUserPO getSystemUserByToken(String token);

    void loginOut(String token);
}