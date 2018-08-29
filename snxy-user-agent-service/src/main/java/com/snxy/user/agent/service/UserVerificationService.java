package com.snxy.user.agent.service;

import com.snxy.user.agent.service.vo.SystemUserVO;

/**
 * @author Administrator
 * @date 2018-08-29
 */
public interface UserVerificationService {
    /**
     * 用户登录系统
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    SystemUserVO login(String userName, String password);

    /**
     * 由token获取用户信息
     * @param token
     * @return
     */
    SystemUserVO getSystemUserByToken(String token);
}