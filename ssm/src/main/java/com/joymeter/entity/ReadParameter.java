package com.joymeter.entity;

import java.io.Serializable;

public class ReadParameter implements Serializable {

    private static final long serialVersionUID = -5896947553297828726L;

    private String parameter_id;
    private String day;
    private String hour;
    private String minute;
    private String second;
    private Double balance_warn;
    private Double valve_close;
    private String isAutoRead;
    private String isAutoInform;

    public String getParameter_id() {
        return parameter_id;
    }

    public void setParameter_id(String parameter_id) {
        this.parameter_id = parameter_id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public Double getBalance_warn() {
        return balance_warn;
    }

    public void setBalance_warn(Double balance_warn) {
        this.balance_warn = balance_warn;
    }

    public Double getValve_close() {
        return valve_close;
    }

    public void setValve_close(Double valve_close) {
        this.valve_close = valve_close;
    }

    public String getIsAutoRead() {
        return isAutoRead;
    }

    public void setIsAutoRead(String isAutoRead) {
        this.isAutoRead = isAutoRead;
    }

    public String getIsAutoInform() {
        return isAutoInform;
    }

    public void setIsAutoInform(String isAutoInform) {
        this.isAutoInform = isAutoInform;
    }

}
