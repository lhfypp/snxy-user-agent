package com.snxy.user.agent.biz.fegin;

import com.snxy.common.response.ResultData;
import org.springframework.stereotype.Component;

/**
 * Created by 24398 on 2018/9/21.
 */

@Component
public class SmsServiceFallBack implements SmsService {

    private static final String msg = "snxy-sms-service 服务降级";

    @Override
    public ResultData sendSmsCode(String mobile, String message, Long messageType) {
        return ResultData.fail("获取验证码失败 ："+msg);
    }
}
