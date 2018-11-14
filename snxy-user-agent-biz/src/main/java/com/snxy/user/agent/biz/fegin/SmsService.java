package com.snxy.user.agent.biz.fegin;


import com.snxy.common.response.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by 24398 on 2018/9/21.
 */
@FeignClient(name="snxy-sms-service",fallbackFactory = SmsServiceFallBack.class)
public interface SmsService {

     @RequestMapping("/sms/sendMessage")
     ResultData sendSmsCode(@RequestParam("mobile") String mobile,@RequestParam("message") String message,
                             @RequestParam("messageType") Long messageType);

}
