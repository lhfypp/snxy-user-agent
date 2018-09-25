package com.snxy.user.agent.service;

import com.snxy.user.agent.domain.SystemLoginLog;
import com.snxy.user.agent.domain.SystemUser;
import com.snxy.user.agent.service.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Future;

public interface SystemLoginLogService {

    Future<Long> writeLoginUserLog(LoginUserVO loginUserVO , SystemUser systemUser, HttpServletRequest request);

    void setLoginSuccess(Long systemLoginLogId);

    boolean log(SystemLoginLog loginLog);

    void setLoginSuccess(SystemLoginLog systemLoginLog);
}
