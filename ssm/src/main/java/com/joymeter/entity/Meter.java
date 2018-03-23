package com.joymeter.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Meter implements Serializable {

    private static final long serialVersionUID = 5815764275757668688L;
    
    private String operator_name;
    private String user_name;
    private String user_address;
    private String contact_information;
    private String meter_type;
    private String fee_type;
    private String meter_no;
    private Double recharge_amount;
    private Double receive_amount;
    private Double use_amount;
    private Double balance;
    private String recharge_id;
    private Timestamp recharge_time;

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getContact_information() {
        return contact_information;
    }

    public void setContact_information(String contact_information) {
        this.contact_information = contact_information;
    }

    public String getMeter_type() {
        return meter_type;
    }

    public void setMeter_type(String meter_type) {
        this.meter_type = meter_type;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getMeter_no() {
        return meter_no;
    }

    public void setMeter_no(String meter_no) {
        this.meter_no = meter_no;
    }

    public Double getRecharge_amount() {
        return recharge_amount;
    }

    public void setRecharge_amount(Double recharge_amount) {
        this.recharge_amount = recharge_amount;
    }

    public Double getReceive_amount() {
        return receive_amount;
    }

    public void setReceive_amount(Double receive_amount) {
        this.receive_amount = receive_amount;
    }

    public Double getUse_amount() {
        return use_amount;
    }

    public void setUse_amount(Double use_amount) {
        this.use_amount = use_amount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getRecharge_id() {
        return recharge_id;
    }

    public void setRecharge_id(String recharge_id) {
        this.recharge_id = recharge_id;
    }

    public Timestamp getRecharge_time() {
        return recharge_time;
    }

    public void setRecharge_time(Timestamp recharge_time) {
        this.recharge_time = recharge_time;
    }

}
