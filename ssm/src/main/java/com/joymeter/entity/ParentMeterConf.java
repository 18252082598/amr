/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.joymeter.entity;

import java.sql.Timestamp;

/**
 *
 * @author Administrator
 */
public class ParentMeterConf {

    private int id;
    private String meter_no;//表号
    private String meter_type;//表类型  0:公共区域母表 1：私有区域母表
    private String allot_type;//分摊类型 0：均摊  1：按比例  2：按人头 3:鼓风机
    private String add_time;//添加时间
    private String operator_account;//操作员账号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeter_no() {
        return meter_no;
    }

    public void setMeter_no(String meter_no) {
        this.meter_no = meter_no;
    }

    public String getMeter_type() {
        return meter_type;
    }

    public void setMeter_type(String meter_type) {
        this.meter_type = meter_type;
    }

    public String getAllot_type() {
        return allot_type;
    }

    public void setAllot_type(String allot_type) {
        this.allot_type = allot_type;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getOperator_account() {
        return operator_account;
    }

    public void setOperator_account(String operator_account) {
        this.operator_account = operator_account;
    }

}
