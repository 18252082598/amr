package com.joymeter.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class HouseCoefficient implements Serializable {

    private static final long serialVersionUID = -5444400241110775450L;

    private Integer coefficient_id;
    private String coefficient_name;
    private Double coefficient;
    private String operator_account;
    private Timestamp add_time;

    public Integer getCoefficient_id() {
        return coefficient_id;
    }

    public void setCoefficient_id(Integer coefficient_id) {
        this.coefficient_id = coefficient_id;
    }

    public String getCoefficient_name() {
        return coefficient_name;
    }

    public void setCoefficient_name(String coefficient_name) {
        this.coefficient_name = coefficient_name;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public String getOperator_account() {
        return operator_account;
    }

    public void setOperator_account(String operator_account) {
        this.operator_account = operator_account;
    }

    public Timestamp getAdd_time() {
        return add_time;
    }

    public void setAdd_time(Timestamp add_time) {
        this.add_time = add_time;
    }
}
