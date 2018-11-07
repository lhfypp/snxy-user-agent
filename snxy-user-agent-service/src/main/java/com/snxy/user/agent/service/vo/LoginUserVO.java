package com.snxy.user.agent.service.vo;


import com.snxy.common.util.CheckUtil;
import com.snxy.common.util.StringUtil;
import lombok.Data;

/**
 * Created by 24398 on 2018/8/30.
 */

@Data
public class LoginUserVO {

    private   String mobile;
    private   String password;
  //  private   Integer loginType ; // 1 -- 手机号   2 -- account 账号
    private   Integer pwdType ;  //  1 --- 验证码  2  ---- 密码
    private   Integer deviceType;



    public void checkParam(){

        CheckUtil.isTrue(StringUtil.isNotBlank(mobile) && StringUtil.isNotBlank(password));
        CheckUtil.isTrue(pwdType == 1 || pwdType == 2,"非法的登陆密码类型");

   }
}
