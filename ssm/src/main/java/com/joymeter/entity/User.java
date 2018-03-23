package com.joymeter.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable {

    private static final long serialVersionUID = -3473027115516322595L;

    private Long user_id;
    private String user_name;
    private String province;
    private String city;
    private String district;
    private String user_address_area;
    private String user_address_community;
    private String user_address_building;
    private String user_address_unit;
    private String user_address_room;
    private String user_address_original_room;
    private String id_card_no;
    private String contact_info;
    private String supplier_name;
    private Double house_area;
    private String coefficient_name;
    private String auto_deduction;
    private String concentrator_name;
    private String meter_no;
    private String meter_status;
    private String user_status;
    private String meter_type;
    private String meter_model;
    private String submeter_no;
    private String valve_no;//阀门编号
    private String valve_status;
    private String protocol_type;
    private String valve_protocol;
    private String fee_type;
    private String operator_account;
    private Timestamp add_time;
    private Double last_balance;
    private Timestamp last_read_time;
    private String pay_remind;
    private int pay_type;
    private int register_status;
    private Double initing_data;
    private String online_synv_code;

    public String getOnline_synv_code() {
        return online_synv_code;
    }

    public void setOnline_synv_code(String online_synv_code) {
        this.online_synv_code = online_synv_code;
    }

    public Double getIniting_data() {
        return initing_data;
    }

    public void setIniting_data(Double initing_data) {
        this.initing_data = initing_data;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long userId) {
        user_id = userId;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String userName) {
        user_name = userName;
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

    public void setUser_address_area(String userAddressArea) {
        user_address_area = userAddressArea;
    }

    public String getUser_address_community() {
        return user_address_community;
    }

    public void setUser_address_community(String userAddressCommunity) {
        user_address_community = userAddressCommunity;
    }

    public String getUser_address_building() {
        return user_address_building;
    }

    public void setUser_address_building(String userAddressBuilding) {
        user_address_building = userAddressBuilding;
    }

    public String getUser_address_unit() {
        return user_address_unit;
    }

    public void setUser_address_unit(String userAddressUnit) {
        user_address_unit = userAddressUnit;
    }

    public String getUser_address_room() {
        return user_address_room;
    }

    public void setUser_address_room(String userAddressRoom) {
        user_address_room = userAddressRoom;
    }

    public String getUser_address_original_room() {
        return user_address_original_room;
    }

    public void setUser_address_original_room(String userAddressOriginalRoom) {
        user_address_original_room = userAddressOriginalRoom;
    }

    public String getId_card_no() {
        return id_card_no;
    }

    public void setId_card_no(String idCardNo) {
        id_card_no = idCardNo;
    }

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contactInfo) {
        contact_info = contactInfo;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplierName) {
        supplier_name = supplierName;
    }

    public Double getHouse_area() {
        return house_area;
    }

    public void setHouse_area(Double houseArea) {
        house_area = houseArea;
    }

    public String getCoefficient_name() {
        return coefficient_name;
    }

    public void setCoefficient_name(String coefficientName) {
        coefficient_name = coefficientName;
    }

    public String getAuto_deduction() {
        return auto_deduction;
    }

    public void setAuto_deduction(String autoDeduction) {
        auto_deduction = autoDeduction;
    }

    public String getConcentrator_name() {
        return concentrator_name;
    }

    public void setConcentrator_name(String concentratorName) {
        concentrator_name = concentratorName;
    }

    public String getMeter_no() {
        return meter_no;
    }

    public void setMeter_no(String meterNo) {
        meter_no = meterNo;
    }

    public String getMeter_status() {
        return meter_status;
    }

    public void setMeter_status(String meterStatus) {
        meter_status = meterStatus;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String userStatus) {
        user_status = userStatus;
    }

    public String getMeter_type() {
        return meter_type;
    }

    public void setMeter_type(String meterType) {
        meter_type = meterType;
    }

    public String getMeter_model() {
        return meter_model;
    }

    public void setMeter_model(String meterModel) {
        meter_model = meterModel;
    }

    public String getSubmeter_no() {
        return submeter_no;
    }

    public void setSubmeter_no(String submeterNo) {
        submeter_no = submeterNo;
    }

    public String getValve_no() {
        return valve_no;
    }

    public void setValve_no(String valveNo) {
        valve_no = valveNo;
    }

    public String getValve_status() {
        return valve_status;
    }

    public void setValve_status(String valve_status) {
        this.valve_status = valve_status;
    }

    public String getProtocol_type() {
        return protocol_type;
    }

    public void setProtocol_type(String protocolType) {
        protocol_type = protocolType;
    }

    public String getValve_protocol() {
        return valve_protocol;
    }

    public void setValve_protocol(String valve_protocol) {
        this.valve_protocol = valve_protocol;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String feeType) {
        fee_type = feeType;
    }

    public String getOperator_account() {
        return operator_account;
    }

    public void setOperator_account(String operatorAccount) {
        operator_account = operatorAccount;
    }

    public Timestamp getAdd_time() {
        return add_time;
    }

    public void setAdd_time(Timestamp addTime) {
        add_time = addTime;
    }

    public Double getLast_balance() {
        return last_balance;
    }

    public void setLast_balance(Double lastBalance) {
        last_balance = lastBalance;
    }

    public Timestamp getLast_read_time() {
        return last_read_time;
    }

    public void setLast_read_time(Timestamp lastReadTime) {
        last_read_time = lastReadTime;
    }

    public String getPay_remind() {
        return pay_remind;
    }

    public void setPay_remind(String payRemind) {
        pay_remind = payRemind;
    }

    public int getRegister_status() {
        return register_status;
    }

    public void setRegister_status(int register_status) {
        this.register_status = register_status;
    }

    @Override
    public String toString() {
        return "User [user_id=" + user_id + ", user_name=" + user_name
                + ", user_address_area=" + user_address_area
                + ", user_address_community=" + user_address_community
                + ", user_address_building=" + user_address_building
                + ", user_address_unit=" + user_address_unit
                + ", user_address_room=" + user_address_room + ", id_card_no="
                + id_card_no + ", contact_info=" + contact_info
                + ", supplier_name=" + supplier_name + ", house_area="
                + house_area + ", coefficient_name=" + coefficient_name
                + ", auto_deduction=" + auto_deduction + ", concentrator_name="
                + concentrator_name + ", meter_no=" + meter_no
                + ", meter_status=" + meter_status + ", user_status="
                + user_status + ", meter_type=" + meter_type + ", meter_model="
                + meter_model + ", submeter_no=" + submeter_no + ", valve_no="
                + valve_no + ", protocol_type=" + protocol_type + ", fee_type="
                + fee_type + ", operator_account=" + operator_account
                + ", add_time=" + add_time + ", last_balance=" + last_balance
                + ", last_read_time=" + last_read_time + ", pay_remind="
                + pay_remind + "]";
    }

}
