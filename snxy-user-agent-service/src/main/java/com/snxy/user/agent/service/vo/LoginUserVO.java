package com.snxy.user.agent.service.vo;

import com.snxy.common.util.CheckUtil;
import com.snxy.common.util.StringUtil;
import lombok.Data;

/**
 * Created by 24398 on 2018/8/30.
 */

@Data
public class LoginUserVO {

    private   String userName;
    private  String password;

    private Integer deviceType;



    public void checkParam(){
        CheckUtil.isTrue(StringUtil.isNotBlank(userName) && StringUtil.isNotBlank(password));
   }
}
