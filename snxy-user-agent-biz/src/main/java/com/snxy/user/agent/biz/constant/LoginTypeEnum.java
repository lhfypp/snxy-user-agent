package com.snxy.user.agent.biz.constant;

/**
 * Created by 24398 on 2018/9/20.
 */
public enum  LoginTypeEnum {

    LOGIN_MOBILE(1,"手机号登陆"),
    LOGIN_ACCOUNT( 2,"账号登陆"),
    LOGIN_SMS(3,"短信登陆");

    private Integer loginType;
    private String loginTypeDesc;

    public Integer getLoginType(){
        return this.loginType;
    }

    public String getLoginTypeDesc(){
        return this.loginTypeDesc;
    }

    LoginTypeEnum(Integer loginType,String loginTypeDesc){
        this.loginType = loginType;
        this.loginTypeDesc = loginTypeDesc;
    }





}
