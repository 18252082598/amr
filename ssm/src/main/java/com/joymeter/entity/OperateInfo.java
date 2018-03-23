package com.joymeter.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class OperateInfo implements Serializable {

    private static final long serialVersionUID = 17457875079464520L;

    private String operate_id;
    private String user_name;
    private String province;
    private String city;
    private String district;
    private String user_address_area;
    private String user_address_community;
    private String user_address_building;
    private String user_address_unit;
    private String user_address_room;
    private String contact_info;
    private String concentrator_name;
    private String meter_model;
    private String meter_type;
    private String meter_no;
    private String fee_type;
    private Double recharge_money;
    private Double recharge_amount_kwh;
    private Double recharge_amount_m3;
    private String recharge_loc;
    private String operate_type;
    private Double balance;
    private String operator_account;
    private String pay_method;
    private String isPrint;
    private Timestamp operate_time;
    private String supplier_name;
    private String id_card_no;//新加
    
    

    public String getId_card_no() {
		return id_card_no;
	}

	public void setId_card_no(String id_card_no) {
		this.id_card_no = id_card_no;
	}

	public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }
    

    public String getOperate_id() {
        return operate_id;
    }

    public void setOperate_id(String operate_id) {
        this.operate_id = operate_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public String getUser_address_area() {
        return user_address_area;
    }

    public void setUser_address_area(String user_address_area) {
        this.user_address_area = user_address_area;
    }

    public String getUser_address_community() {
        return user_address_community;
    }

    public void setUser_address_community(String user_address_community) {
        this.user_address_community = user_address_community;
    }

    public String getUser_address_building() {
        return user_address_building;
    }

    public void setUser_address_building(String user_address_building) {
        this.user_address_building = user_address_building;
    }

    public String getUser_address_unit() {
        return user_address_unit;
    }

    public void setUser_address_unit(String user_address_unit) {
        this.user_address_unit = user_address_unit;
    }

    public String getUser_address_room() {
        return user_address_room;
    }

    public void setUser_address_room(String user_address_room) {
        this.user_address_room = user_address_room;
    }

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }

    public String getConcentrator_name() {
        return concentrator_name;
    }

    public void setConcentrator_name(String concentrator_name) {
        this.concentrator_name = concentrator_name;
    }

    public String getMeter_type() {
        return meter_type;
    }

    public void setMeter_type(String meter_type) {
        this.meter_type = meter_type;
    }

    public String getMeter_no() {
        return meter_no;
    }

    public void setMeter_no(String meter_no) {
        this.meter_no = meter_no;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public Double getRecharge_money() {
        return recharge_money;
    }

    public void setRecharge_money(Double recharge_money) {
        this.recharge_money = recharge_money;
    }

    public Double getRecharge_amount_kwh() {
        return recharge_amount_kwh;
    }

    public void setRecharge_amount_kwh(Double recharge_amount_kwh) {
        this.recharge_amount_kwh = recharge_amount_kwh;
    }

    public Double getRecharge_amount_m3() {
        return recharge_amount_m3;
    }

    public void setRecharge_amount_m3(Double recharge_amount_m3) {
        this.recharge_amount_m3 = recharge_amount_m3;
    }

    public String getOperate_type() {
        return operate_type;
    }

    public void setOperate_type(String operate_type) {
        this.operate_type = operate_type;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getOperator_account() {
        return operator_account;
    }

    public void setOperator_account(String operator_account) {
        this.operator_account = operator_account;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    public String getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(String isPrint) {
        this.isPrint = isPrint;
    }

    public Timestamp getOperate_time() {
        return operate_time;
    }

    public void setOperate_time(Timestamp operate_time) {
        this.operate_time = operate_time;
    }

    public String getRecharge_loc() {
        return recharge_loc;
    }

    public void setRecharge_loc(String recharge_loc) {
        this.recharge_loc = recharge_loc;
    }

    public String getMeter_model() {
        return meter_model;
    }

    public void setMeter_model(String meter_model) {
        this.meter_model = meter_model;
    }
}
