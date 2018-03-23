package com.joymeter.entity;

import java.io.Serializable;

public class Role implements Serializable {

    private static final long serialVersionUID = 6416648470685904074L;

    private Long id;
    private String role_name;
    private String permission;
    private String role_add_time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getRole_add_time() {
        return role_add_time;
    }

    public void setRole_add_time(String role_add_time) {
        this.role_add_time = role_add_time;
    }
}
