package com.joymeter.entity;

import java.io.Serializable;

public class Room implements Serializable {

    private static final long serialVersionUID = -3473027115516322595L;

    private String user_address_room;
    private String meter_no;
    private String meter_type;

    public Room(String user_address_room, String meter_no, String meter_type) {
        this.user_address_room = user_address_room;
        this.meter_no = meter_no;
        this.meter_type = meter_type;
    }

    public String getMeter_type() {
        return meter_type;
    }

    public void setMeter_type(String meter_type) {
        this.meter_type = meter_type;
    }

    public String getUser_address_room() {
        return user_address_room;
    }

    public void setUser_address_room(String userAddressRoom) {
        user_address_room = userAddressRoom;
    }

    public String getMeter_no() {
        return meter_no;
    }

    public void setMeter_no(String meterNo) {
        meter_no = meterNo;
    }
}
