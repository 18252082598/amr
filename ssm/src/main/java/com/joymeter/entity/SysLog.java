package com.joymeter.entity;

import java.io.Serializable;

public class SysLog implements Serializable {

    private static final long serialVersionUID = -3009804213053820960L;

    private Integer id;
    private String loginName;
    private String userName;
    private String operatingcontent;
    private String operateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOperatingcontent() {
        return operatingcontent;
    }

    public void setOperatingcontent(String operatingcontent) {
        this.operatingcontent = operatingcontent;
    }

    public String getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(String operateDate) {
        this.operateDate = operateDate;
    }

    @Override
    public String toString() {
        return "SysLog [id=" + id + ", loginName=" + loginName
                + ", operatingcontent=" + operatingcontent + ", operateDate=" + operateDate + "]";
    }

}
