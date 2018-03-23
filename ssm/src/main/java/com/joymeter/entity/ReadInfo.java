package com.joymeter.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class ReadInfo implements Serializable {

    private static final long serialVersionUID = 8557474217663233181L;

    private Long info_id;
    private String operate_id;
    private String user_name;
    private String user_address;
    private String user_address_area;
    private String user_address_community;
    private String community_no;
    private String user_address_building;
    private String user_address_unit;
    private String user_address_room;
    private String user_address_original_room;
    private String contact_info;
    private String concentrator_name;
    private String meter_no;
    private String fee_type;
    private Double this_read;
    private Double last_read;
    private Double this_cost;
    private Double last_cost;
    private Double fee_need;
    private Double balance;
    private String exception;
    private String fee_status;
    private String read_type;
    private String pay_remind;
    private String operator_account;
    private Timestamp operate_time;
    private String operate_day;
    private String operate_dayTime;
    private String meter_type;
    private Double data2;
    private Double data3;
    private Double data4;
    private Double data5;
    private Double data6;
    private Double data7;
    private Double data8;
    private String data9;
    private String isAutoClear;
    private String supplier_name;

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public Long getInfo_id() {
        return info_id;
    }

    public void setInfo_id(Long info_id) {
        this.info_id = info_id;
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

    /**
     * @return the community_no
     */
    public String getCommunity_no() {
        return community_no;
    }

    /**
     * @param community_no the community_no to set
     */
    public void setCommunity_no(String community_no) {
        this.community_no = community_no;
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

    public String getUser_address_original_room() {
        return user_address_original_room;
    }

    public void setUser_address_original_room(String user_address_original_room) {
        this.user_address_original_room = user_address_original_room;
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

    public Double getThis_read() {
        return this_read;
    }

    public void setThis_read(Double this_read) {
        this.this_read = this_read;
    }

    public Double getLast_read() {
        return last_read;
    }

    public void setLast_read(Double last_read) {
        this.last_read = last_read;
    }

    public Double getThis_cost() {
        return this_cost;
    }

    public void setThis_cost(Double this_cost) {
        this.this_cost = this_cost;
    }

    public Double getLast_cost() {
        return last_cost;
    }

    public void setLast_cost(Double last_cost) {
        this.last_cost = last_cost;
    }

    public Double getFee_need() {
        return fee_need;
    }

    public void setFee_need(Double fee_need) {
        this.fee_need = fee_need;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getFee_status() {
        return fee_status;
    }

    public void setFee_status(String fee_status) {
        this.fee_status = fee_status;
    }

    public String getRead_type() {
        return read_type;
    }

    public void setRead_type(String read_type) {
        this.read_type = read_type;
    }

    public void setPay_remind(String pay_remind) {
        this.pay_remind = pay_remind;
    }

    public String getPay_remind() {
        return pay_remind;
    }

    public String getOperator_account() {
        return operator_account;
    }

    public void setOperator_account(String operator_account) {
        this.operator_account = operator_account;
    }

    public Timestamp getOperate_time() {
        return operate_time;
    }

    public void setOperate_time(Timestamp operate_time) {
        this.operate_time = operate_time;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getOperate_day() {
        return operate_day;
    }

    public void setOperate_day(String operate_day) {
        this.operate_day = operate_day;
    }

    public String getOperate_dayTime() {
        return operate_dayTime;
    }

    public void setOperate_dayTime(String operate_datTime) {
        this.operate_dayTime = operate_datTime;
    }

    public String getMeter_type() {
        return meter_type;
    }

    public void setMeter_type(String meter_type) {
        this.meter_type = meter_type;
    }

    /**
     * @return the data2
     */
    public Double getData2() {
        return data2;
    }

    /**
     * @param data2 the data2 to set
     */
    public void setData2(Double data2) {
        this.data2 = data2;
    }

    /**
     * @return the data3
     */
    public Double getData3() {
        return data3;
    }

    /**
     * @param data3 the data3 to set
     */
    public void setData3(Double data3) {
        this.data3 = data3;
    }

    /**
     * @return the data4
     */
    public Double getData4() {
        return data4;
    }

    /**
     * @param data4 the data4 to set
     */
    public void setData4(Double data4) {
        this.data4 = data4;
    }

    /**
     * @return the data5
     */
    public Double getData5() {
        return data5;
    }

    /**
     * @param data5 the data5 to set
     */
    public void setData5(Double data5) {
        this.data5 = data5;
    }

    /**
     * @return the data6
     */
    public Double getData6() {
        return data6;
    }

    /**
     * @param data6 the data6 to set
     */
    public void setData6(Double data6) {
        this.data6 = data6;
    }

    /**
     * @return the data7
     */
    public Double getData7() {
        return data7;
    }

    /**
     * @param data7 the data7 to set
     */
    public void setData7(Double data7) {
        this.data7 = data7;
    }

    /**
     * @return the data8
     */
    public Double getData8() {
        return data8;
    }

    /**
     * @param data8 the data8 to set
     */
    public void setData8(Double data8) {
        this.data8 = data8;
    }

    /**
     * @return the data9
     */
    public String getData9() {
        return data9;
    }

    /**
     * @param data9 the data9 to set
     */
    public void setData9(String data9) {
        this.data9 = data9;
    }

    /**
     * @return the isAutoClear
     */
    public String getIsAutoClear() {
        return isAutoClear;
    }

    /**
     * @param isAutoClear the isAutoClear to set
     */
    public void setIsAutoClear(String isAutoClear) {
        this.isAutoClear = isAutoClear;
    }

}
