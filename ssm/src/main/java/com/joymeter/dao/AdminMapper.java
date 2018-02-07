package com.joymeter.dao;

import com.joymeter.entity.Admin;

public interface AdminMapper {
    
    Admin getAdminByName(String username);
}
