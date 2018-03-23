package com.joymeter.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Building implements Serializable {

    private static final long serialVersionUID = -4238232482424147332L;

    private Long building_id;
    private String province;
    private String city;
    private String district;
    private String community_name;
    private String building_name;
    private String operator_account;
    private Timestamp add_time;

    public Long getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(Long buildingId) {
        building_id = buildingId;
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

    public Timestamp getAdd_time() {
        return add_time;
    }

    public void setAdd_time(Timestamp addTime) {
        add_time = addTime;
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

    public void setOperator_account(String operator_account) {
        this.operator_account = operator_account;
    }

    public String getOperator_account() {
        return operator_account;
    }

}
