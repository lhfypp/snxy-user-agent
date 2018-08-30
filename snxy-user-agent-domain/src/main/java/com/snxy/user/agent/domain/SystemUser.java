package com.snxy.user.agent.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemUser {
    private Long id;

    private String account;

    private String mobile;

    private String chineseName;

    private String nick;

    private String pwd;

    private Byte gender;

    private Date regDate;

    private Date gmtCreate;

    private Date gmtModified;

    private Byte accountStatus;
   

}