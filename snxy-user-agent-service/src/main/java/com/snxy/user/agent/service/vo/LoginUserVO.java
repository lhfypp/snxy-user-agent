package com.snxy.user.agent.service.vo;


import com.snxy.common.util.CheckUtil;
import com.snxy.common.util.StringUtil;
import lombok.Data;

/**
 * Created by 24398 on 2018/8/30.
 */

@Data
public class LoginUserVO {

    private   String username;
    private  String password;
    private  Integer loginType ; // 1 -- 手机号   2 -- account 账号
    private Integer deviceType;



    public void checkParam(){
        CheckUtil.isTrue(StringUtil.isNotBlank(username) && StringUtil.isNotBlank(password));
   }
}
