package com.joymeter.entity;

import java.util.Date;

public class Wechat {
    private Integer wechatid;

    private Integer userid;

    private Integer cjoyid;

    private String wechatname;

    private String appid;

    private String appsecret;

    private String mchid;

    private String apikey;

    private String token;

    private String encondingasekey;

    private String encodingstyle;

    private Integer status;

    private Date createtime;

    private String welcomemsg;

    private String notifyrecharge;

    private String notifynomoney;

    private String reserved1;

    private String reserved2;

    private String reserved3;

    public Integer getWechatid() {
        return wechatid;
    }

    public void setWechatid(Integer wechatid) {
        this.wechatid = wechatid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getCjoyid() {
        return cjoyid;
    }

    public void setCjoyid(Integer cjoyid) {
        this.cjoyid = cjoyid;
    }

    public String getWechatname() {
        return wechatname;
    }

    public void setWechatname(String wechatname) {
        this.wechatname = wechatname;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEncondingasekey() {
        return encondingasekey;
    }

    public void setEncondingasekey(String encondingasekey) {
        this.encondingasekey = encondingasekey;
    }

    public String getEncodingstyle() {
        return encodingstyle;
    }

    public void setEncodingstyle(String encodingstyle) {
        this.encodingstyle = encodingstyle;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getWelcomemsg() {
        return welcomemsg;
    }

    public void setWelcomemsg(String welcomemsg) {
        this.welcomemsg = welcomemsg;
    }

    public String getNotifyrecharge() {
        return notifyrecharge;
    }

    public void setNotifyrecharge(String notifyrecharge) {
        this.notifyrecharge = notifyrecharge;
    }

    public String getNotifynomoney() {
        return notifynomoney;
    }

    public void setNotifynomoney(String notifynomoney) {
        this.notifynomoney = notifynomoney;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    @Override
    public String toString() {
        return "Wechat [wechatid=" + wechatid + ", userid=" + userid + ", cjoyid=" + cjoyid + ", wechatname="
                + wechatname + ", appid=" + appid + ", appsecret=" + appsecret + ", mchid=" + mchid + ", apikey="
                + apikey + ", token=" + token + ", encondingasekey=" + encondingasekey + ", encodingstyle="
                + encodingstyle + ", status=" + status + ", createtime=" + createtime + ", welcomemsg=" + welcomemsg
                + ", notifyrecharge=" + notifyrecharge + ", notifynomoney=" + notifynomoney + ", reserved1=" + reserved1
                + ", reserved2=" + reserved2 + ", reserved3=" + reserved3 + "]";
    }
    
    
}
