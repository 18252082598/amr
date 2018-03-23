package com.joymeter.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Concentrator implements Serializable {

    private static final long serialVersionUID = 8110207854245420889L;

    private Integer concentrator_id;
    private String concentrator_name;
    private String concentrator_no;
    private String gateway_id;
    private String concentrator_ip;
    private String concentrator_port;
    private String concentrator_model;
    private String concentrator_state;
    private String DTU_sim_no;
    private String user_address_community;
    private String user_address_building;
    private String user_address_unit;
    private String operator_account;
    private String public_key;
    private Timestamp add_time;

    public String getConcentrator_model() {
        return concentrator_model;
    }

    public void setConcentrator_model(String concentrator_model) {
        this.concentrator_model = concentrator_model;
    }

    public Integer getConcentrator_id() {
        return concentrator_id;
    }

    public void setConcentrator_id(Integer concentrator_id) {
        this.concentrator_id = concentrator_id;
    }

    public String getConcentrator_name() {
        return concentrator_name;
    }

    public void setConcentrator_name(String concentrator_name) {
        this.concentrator_name = concentrator_name;
    }

    public String getConcentrator_no() {
        return concentrator_no;
    }

    public void setConcentrator_no(String concentratorNo) {
        concentrator_no = concentratorNo;
    }

    public String getGateway_id() {
        return gateway_id;
    }

    public void setGateway_id(String gatewayId) {
        gateway_id = gatewayId;
    }

    public String getConcentrator_ip() {
        return concentrator_ip;
    }

    public void setConcentrator_ip(String concentrator_ip) {
        this.concentrator_ip = concentrator_ip;
    }

    public String getConcentrator_port() {
        return concentrator_port;
    }

    public void setConcentrator_port(String concentrator_port) {
        this.concentrator_port = concentrator_port;
    }

    public void setConcentrator_state(String concentrator_state) {
        this.concentrator_state = concentrator_state;
    }

    public String getConcentrator_state() {
        return concentrator_state;
    }

    public String getDTU_sim_no() {
        return DTU_sim_no;
    }

    public void setDTU_sim_no(String dTU_sim_no) {
        DTU_sim_no = dTU_sim_no;
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

    public String getOperator_account() {
        return operator_account;
    }

    public void setOperator_account(String operator_account) {
        this.operator_account = operator_account;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public Timestamp getAdd_time() {
        return add_time;
    }

    public void setAdd_time(Timestamp add_time) {
        this.add_time = add_time;
    }
}
