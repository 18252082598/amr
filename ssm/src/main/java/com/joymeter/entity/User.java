package com.joymeter.entity;

import java.util.Date;
import java.util.List;

public class User {
    private Integer userid;

    private String username;

    private String pwd;

    private String name;

    private String tel;

    private String address;

    private Integer status;

    private String email;

    private String remake;

    private Date createtime;

    private String reserved1;

    private String reserved2;

    private String reserved3;

    private List<Wechat> wechats;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake == null ? null : remake.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1 == null ? null : reserved1.trim();
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2 == null ? null : reserved2.trim();
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3 == null ? null : reserved3.trim();
    }

    public List<Wechat> getWechats() {
        return wechats;
    }

    public void setWechats(List<Wechat> wechats) {
        this.wechats = wechats;
    }

    @Override
    public String toString() {
        return "User [userid=" + userid + ", username=" + username + ", pwd=" + pwd + ", name=" + name + ", tel=" + tel
                + ", address=" + address + ", status=" + status + ", email=" + email + ", remake=" + remake
                + ", createtime=" + createtime + ", reserved1=" + reserved1 + ", reserved2=" + reserved2
                + ", reserved3=" + reserved3 + ", wechats=" + wechats + "]";
    }

}