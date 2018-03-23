package com.joymeter.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Community implements Serializable {

    private static final long serialVersionUID = 4052910579334609251L;

    private Long community_id;
    private String province;
    private String city;
    private String district;
    private String community_no;
    private String community_name;
    private String operator_account;
    private Timestamp add_time;

    public Long getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(Long communityId) {
        community_id = communityId;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getCommunity_name() {
        return community_name;
    }

    public void setCommunity_name(String communityName) {
        community_name = communityName;
    }

    public String getOperator_account() {
        return operator_account;
    }

    public void setOperator_account(String operatorAccount) {
        operator_account = operatorAccount;
    }

    public void setAdd_time(Timestamp add_time) {
        this.add_time = add_time;
    }

    public Timestamp getAdd_time() {
        return add_time;
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

}
