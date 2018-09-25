package com.snxy.user.agent.biz.serviceImpl;

import com.snxy.user.agent.biz.constant.LoginDeviceEnum;
import com.snxy.user.agent.dao.mapper.SystemLoginLogMapper;
import com.snxy.user.agent.domain.SystemLoginLog;
import com.snxy.user.agent.domain.SystemUser;
import com.snxy.user.agent.service.SystemLoginLogService;
import com.snxy.user.agent.service.vo.LoginUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.Future;

@Service
@Slf4j
public class SystemLoginLogServiceImpl implements SystemLoginLogService {

    @Resource
    private SystemLoginLogMapper systemLoginLogMapper;


    @Override
    @Async
    public Future<Long> writeLoginUserLog(LoginUserVO loginUserVO, SystemUser systemUser, HttpServletRequest request) {
        Long systemUserId = systemUser.getId();
        String ip = request.getRemoteAddr();
        SystemLoginLog loginLog = new SystemLoginLog();
        loginLog.setUserId(systemUserId);
        loginLog.setGmtCreate(new Date());
        loginLog.setIp(ip);
        loginLog.setNote("");
        loginLog.setIsSuccess((byte)0);  // 默认没有成功
        loginLog.setIsDelete((byte)1);
        loginLog.setLoginTypeCode(LoginDeviceEnum.getDeviceDesc(loginUserVO.getDeviceType()));

        this.log(loginLog);
        Long threadId =  Thread.currentThread().getId();
        String threadName = Thread.currentThread().getName();
        log.info("writeLoginUserLog  threadId : [{}]  ; threadName : [{}]",threadId,threadName);
        return new AsyncResult<>(loginLog.getId());

    }

    @Override
    @Async
    public void setLoginSuccess(Long systemLoginLogId) {
        if(systemLoginLogId == null){
            return;
        }
        SystemLoginLog systemLoginLog  = new SystemLoginLog();
        systemLoginLog.setId(systemLoginLogId);
        systemLoginLog.setIsSuccess((byte)1);
        Long threadId =  Thread.currentThread().getId();
        String threadName = Thread.currentThread().getName();
        log.info("setLoginSuccess  threadId : [{}]  ; threadName : [{}]",threadId,threadName);
        this.setLoginSuccess(systemLoginLog);
    }

    @Override
    public boolean log(SystemLoginLog loginLog) {
        this.systemLoginLogMapper.insertSelective(loginLog);
        return false;
    }

    @Override
    public void setLoginSuccess(SystemLoginLog systemLoginLog) {
        this.systemLoginLogMapper.updateByPrimaryKeySelective(systemLoginLog);
    }
}
