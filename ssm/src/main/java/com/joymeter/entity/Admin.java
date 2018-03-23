package com.joymeter.entity;

import java.io.Serializable;

public class Admin implements Serializable {

    private static final long serialVersionUID = -8297796731750872175L;

    private Long admin_id;
    private Long role_id;
    private String admin_account;
    private String admin_name;
    private String admin_tel;
    private String admin_supplier_name;
    private String admin_password;
    private String admin_power;
    private String account_status;
    private String admin_add_time;
    private String last_load_time;

    public Long getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(Long admin_id) {
        this.admin_id = admin_id;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public String getAdmin_account() {
        return admin_account;
    }

    public void setAdmin_account(String admin_account) {
        this.admin_account = admin_account;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getAdmin_tel() {
        return admin_tel;
    }

    public void setAdmin_tel(String admin_tel) {
        this.admin_tel = admin_tel;
    }

    public String getAdmin_supplier_name() {
        return admin_supplier_name;
    }

    public void setAdmin_supplier_name(String admin_supplier_name) {
        this.admin_supplier_name = admin_supplier_name;
    }

    public String getAdmin_password() {
        return admin_password;
    }

    public void setAdmin_password(String admin_password) {
        this.admin_password = admin_password;
    }

    public String getAdmin_power() {
        return admin_power;
    }

    public void setAdmin_power(String admin_power) {
        this.admin_power = admin_power;
    }

    public String getAccount_status() {
        return account_status;
    }

    public void setAccount_status(String account_status) {
        this.account_status = account_status;
    }

    public String getAdmin_add_time() {
        return admin_add_time;
    }

    public void setAdmin_add_time(String admin_add_time) {
        this.admin_add_time = admin_add_time;
    }

    public String getLast_load_time() {
        return last_load_time;
    }

    public void setLast_load_time(String last_load_time) {
        this.last_load_time = last_load_time;
    }   
}
