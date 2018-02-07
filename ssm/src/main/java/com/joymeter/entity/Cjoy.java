package com.joymeter.entity;

import java.util.Date;

public class Cjoy {
    private Integer cjoyid;

    private String cjoyname;

    private Date createtime;

    private String paykey;

    private String payscret;

    private String cjoypath;

    private String finduser;

    private String findenergy;

    private String finddata;

    private String payurl;

    private String reserved1;

    private String reserved2;

    private String reserved3;

    private String reserved4;

    public Integer getCjoyid() {
        return cjoyid;
    }

    public void setCjoyid(Integer cjoyid) {
        this.cjoyid = cjoyid;
    }

    public String getCjoyname() {
        return cjoyname;
    }

    public void setCjoyname(String cjoyname) {
        this.cjoyname = cjoyname == null ? null : cjoyname.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getPaykey() {
        return paykey;
    }

    public void setPaykey(String paykey) {
        this.paykey = paykey == null ? null : paykey.trim();
    }

    public String getPayscret() {
        return payscret;
    }

    public void setPayscret(String payscret) {
        this.payscret = payscret == null ? null : payscret.trim();
    }

    public String getCjoypath() {
        return cjoypath;
    }

    public void setCjoypath(String cjoypath) {
        this.cjoypath = cjoypath == null ? null : cjoypath.trim();
    }

    public String getFinduser() {
        return finduser;
    }

    public void setFinduser(String finduser) {
        this.finduser = finduser == null ? null : finduser.trim();
    }

    public String getFindenergy() {
        return findenergy;
    }

    public void setFindenergy(String findenergy) {
        this.findenergy = findenergy == null ? null : findenergy.trim();
    }

    public String getFinddata() {
        return finddata;
    }

    public void setFinddata(String finddata) {
        this.finddata = finddata == null ? null : finddata.trim();
    }

    public String getPayurl() {
        return payurl;
    }

    public void setPayurl(String payurl) {
        this.payurl = payurl == null ? null : payurl.trim();
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

    public String getReserved4() {
        return reserved4;
    }

    public void setReserved4(String reserved4) {
        this.reserved4 = reserved4 == null ? null : reserved4.trim();
    }
}