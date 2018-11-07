package com.snxy.user.agent.biz.constant;

/**
 * Created by 24398 on 2018/11/5.
 */
public enum IdentityTypeEnum {

    VISITOR(4,"随便看看"),
    DRIVER(3,"司机"),
    Employee(2,"员工"),
    CHARGE_PERSON(1,"负责人");
    // PC (5,"电脑端");

    private Integer id ;
    private String identityName;

    public Integer getId(){return  this.id;}
    public String getIdentityName(){return  this.identityName;}


    IdentityTypeEnum(Integer id,String identityName){
        this.id = id;
        this.identityName = identityName;
    }

    public static String getIdentityName(Integer id){
        if(id == null){
            return null;
        }

        IdentityTypeEnum [] identityTypeEnums = IdentityTypeEnum.values();

        for(int i =0;i < identityTypeEnums.length;i++){
            if( identityTypeEnums[i].getId() == id ){
                return identityTypeEnums[i].getIdentityName();
            }
        }

        return null;
    }




}
