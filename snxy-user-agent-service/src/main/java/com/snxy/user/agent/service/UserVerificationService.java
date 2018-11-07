package com.snxy.user.agent.service;

import com.snxy.user.agent.service.vo.LoginUserVO;
import com.snxy.user.agent.service.vo.SystemUserVO;

import java.util.Map;

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
    Map<String,Object> loginOrRegistry(LoginUserVO loginUserVO);

    /**
     * 由token获取用户信息
     * @param token
     * @return
     */
    SystemUserVO getSystemUserByToken(String token);

    void loginOut(String token);



    String getSmsCode(String mobile);

    SystemUserVO changCacheUser(Long systemUserId);
}