package com.joymeter.entity;

import java.io.Serializable;

public class Operator implements Serializable {

    private static final long serialVersionUID = -2024117769762073371L;
    
    private Long operator_id;
    private String operator_account;
    private String password;
    private String operator_name;
    private String contact_info;
    private String operator_email;
    private String operator_authority;
    private String account_status;
    private String loc_name;

    public Long getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(Long operator_id) {
        this.operator_id = operator_id;
    }

    public void setOperator_account(String operator_account) {
        this.operator_account = operator_account;
    }

    public String getOperator_account() {
        return operator_account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }

    public String getOperator_email() {
        return operator_email;
    }

    public void setOperator_email(String operator_email) {
        this.operator_email = operator_email;
    }

    public String getOperator_authority() {
        return operator_authority;
    }

    public void setOperator_authority(String operator_authority) {
        this.operator_authority = operator_authority;
    }

    public String getAccount_status() {
        return account_status;
    }

    public void setAccount_status(String account_status) {
        this.account_status = account_status;
    }

    public String getLoc_name() {
        return loc_name;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }

}
