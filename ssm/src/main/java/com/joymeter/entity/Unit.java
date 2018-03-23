package com.joymeter.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Unit implements Serializable {

    private static final long serialVersionUID = 187330492175956529L;

    private Long unit_id;
    private String province;
    private String city;
    private String district;
    private String community_name;
    private String building_name;
    private String unit_name;
    private String operator_account;
    private Timestamp add_time;

    public Long getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(Long unitId) {
        unit_id = unitId;
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

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String communityName) {
        community_name = communityName;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String buildingName) {
        building_name = buildingName;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unitName) {
        unit_name = unitName;
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

}
