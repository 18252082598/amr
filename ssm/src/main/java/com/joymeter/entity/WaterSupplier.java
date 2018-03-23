package com.joymeter.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class WaterSupplier implements Serializable {

    private static final long serialVersionUID = -5156669545015382729L;
    
    private Integer supplier_id;
    private String supplier_name;
    private String province;
    private String city;
    private String district;
    private String supplier_address;
    private Timestamp add_time;
    private String operator_name;

    public Integer getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(Integer supplierId) {
        supplier_id = supplierId;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplierName) {
        supplier_name = supplierName;
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

    public String getSupplier_address() {
        return supplier_address;
    }

    public void setSupplier_address(String supplierAddress) {
        supplier_address = supplierAddress;
    }

    public Timestamp getAdd_time() {
        return add_time;
    }

    public void setAdd_time(Timestamp addTime) {
        add_time = addTime;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operatorName) {
        operator_name = operatorName;
    }

}
