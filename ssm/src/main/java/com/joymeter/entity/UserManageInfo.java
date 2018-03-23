package com.joymeter.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserManageInfo implements Serializable {

    private static final long serialVersionUID = -5765672472292762542L;

    private Long info_id;
    private String user_name;
    private String meter_no;
    private String province;
    private String city;
    private String district;
    private String user_address;
    private String contact_info;
    private String info_content;
    private String new_meter_no;
    private Double closingCost;
    private String operator_account;
    private Timestamp operate_time;

    public Long getInfo_id() {
        return info_id;
    }

    public void setInfo_id(Long infoId) {
        info_id = infoId;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String userName) {
        user_name = userName;
    }

    public void setMeter_no(String meter_no) {
        this.meter_no = meter_no;
    }

    public String getMeter_no() {
        return meter_no;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String userAddress) {
        user_address = userAddress;
    }

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contactInfo) {
        contact_info = contactInfo;
    }

    public String getInfo_content() {
        return info_content;
    }

    public void setInfo_content(String infoContent) {
        info_content = infoContent;
    }

    public void setNew_meter_no(String new_meter_no) {
        this.new_meter_no = new_meter_no;
    }

    public void setClosingCost(Double closingCost) {
        this.closingCost = closingCost;
    }

    public Double getClosingCost() {
        return closingCost;
    }

    public String getNew_meter_no() {
        return new_meter_no;
    }

    public String getOperator_account() {
        return operator_account;
    }

    public void setOperator_account(String operatorAccount) {
        operator_account = operatorAccount;
    }

    public void setOperate_time(Timestamp operate_time) {
        this.operate_time = operate_time;
    }

    public Timestamp getOperate_time() {
        return operate_time;
    }

}
