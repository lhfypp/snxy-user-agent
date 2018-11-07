package com.snxy.user.agent.biz.constant;

/**
 * Created by 24398 on 2018/8/30.
 */
public enum  LoginDeviceEnum {

    PHONE_ANDROID(1,"安卓手机"),
    PHONE_IOS(2,"苹果手机"),
    PAD_ANDROID(3,"安卓平板"),
    PAD_IOS(4,"苹果平板");
   // PC (5,"电脑端");

    private Integer deviceType ;
    private String deviceDesc;

    public Integer getDeviceType(){return this.deviceType;}
    public String getDeviceDesc(){return this.deviceDesc;}


    LoginDeviceEnum(Integer deviceType,String deviceDesc){
        this.deviceType = deviceType;
        this.deviceDesc = deviceDesc;
    }

    public static String getDeviceDesc(Integer deviceType){
        if(deviceType == null){
            return null;
        }

        LoginDeviceEnum [] loginDeviceEnums = LoginDeviceEnum.values();

        for(int i =0;i < loginDeviceEnums.length;i++){
            if( loginDeviceEnums[i].getDeviceType() == deviceType ){
                return loginDeviceEnums[i].getDeviceDesc();
            }
        }

         return null;
    }


    public static boolean containType(Integer deviceType){
        if(deviceType == null){
            return false;
        }

        LoginDeviceEnum [] loginDeviceEnums = LoginDeviceEnum.values();

        for(int i =0;i < loginDeviceEnums.length;i++){
            if( loginDeviceEnums[i].getDeviceType() == deviceType ){
                return true;
            }
        }

        return false;
    }


}
