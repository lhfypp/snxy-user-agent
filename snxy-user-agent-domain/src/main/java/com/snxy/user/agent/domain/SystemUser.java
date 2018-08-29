package com.snxy.user.agent.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getAccount() {
//        return account;
//    }
//
//    public void setAccount(String account) {
//        this.account = account;
//    }
//
//    public String getMobile() {
//        return mobile;
//    }
//
//    public void setMobile(String mobile) {
//        this.mobile = mobile;
//    }
//
//    public String getChineseName() {
//        return chineseName;
//    }
//
//    public void setChineseName(String chineseName) {
//        this.chineseName = chineseName;
//    }
//
//    public String getNick() {
//        return nick;
//    }
//
//    public void setNick(String nick) {
//        this.nick = nick;
//    }
//
//    public String getPwd() {
//        return pwd;
//    }
//
//    public void setPwd(String pwd) {
//        this.pwd = pwd;
//    }
//
//    public Byte getGender() {
//        return gender;
//    }
//
//    public void setGender(Byte gender) {
//        this.gender = gender;
//    }
//
//    public Date getRegDate() {
//        return regDate;
//    }
//
//    public void setRegDate(Date regDate) {
//        this.regDate = regDate;
//    }
//
//    public Date getGmtCreate() {
//        return gmtCreate;
//    }
//
//    public void setGmtCreate(Date gmtCreate) {
//        this.gmtCreate = gmtCreate;
//    }
//
//    public Date getGmtModified() {
//        return gmtModified;
//    }
//
//    public void setGmtModified(Date gmtModified) {
//        this.gmtModified = gmtModified;
//    }
//
//    public Byte getAccountStatus() {
//        return accountStatus;
//    }
//
//    public void setAccountStatus(Byte accountStatus) {
//        this.accountStatus = accountStatus;
//    }
}