package com.joymeter.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Record implements Serializable {

    private static final long serialVersionUID = 2289929030519810520L;
    
    private Long record_id;
    private String record;
    private String operator_account;
    private Timestamp operate_time;

    public Long getRecord_id() {
        return record_id;
    }

    public void setRecord_id(Long record_id) {
        this.record_id = record_id;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
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

}
